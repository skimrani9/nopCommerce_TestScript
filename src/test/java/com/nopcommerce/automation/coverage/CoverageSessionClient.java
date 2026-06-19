package com.nopcommerce.automation.coverage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CoverageSessionClient {

    private static final Logger logger = LogManager.getLogger(CoverageSessionClient.class);
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final Pattern MISSING_ENDS_PATTERN = Pattern.compile("\"missingEnds\"\\s*:\\s*\\[([^\\]]*)\\]");
    private static final Pattern IS_VALID_PATTERN = Pattern.compile("\"isValid\"\\s*:\\s*(true|false)");
    private static final Pattern ACTIVE_SESSION_PATTERN =
            Pattern.compile("Another coverage session is active:\\s*([\\w-]+)");
    private static final Pattern SESSION_ID_PATTERN = Pattern.compile("\"sessionId\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern STATUS_PATTERN = Pattern.compile("\"status\"\\s*:\\s*(\"?)(\\w+)\\1");

    private final String baseUrl;
    private final boolean enabled;
    private final boolean failOnMissingTcEnd;
    private boolean joinedExistingSession;

    public CoverageSessionClient() {
        this.enabled = Boolean.parseBoolean(System.getProperty("coverage.enabled", "false"));
        this.baseUrl = trimTrailingSlash(System.getProperty("coverage.api.baseUrl", "http://127.0.0.1:5055"));
        this.failOnMissingTcEnd = Boolean.parseBoolean(System.getProperty("coverage.failOnMissingTcEnd", "true"));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String createSession() {
        return postJson("/sessions", "{}", null).body();
    }

    public String resolveSessionId() {
        String configured = System.getProperty("coverage.session.id", "").trim();
        if (!configured.isBlank()) {
            joinedExistingSession = true;
            logger.info("Using configured coverage session id: {}", configured);
            return configured;
        }

        try {
            joinedExistingSession = false;
            return extractSessionId(createSession());
        } catch (IllegalStateException exception) {
            String activeSessionId = extractActiveSessionId(exception);
            if (activeSessionId != null && isSessionRunning(activeSessionId)) {
                joinedExistingSession = true;
                logger.info("Joining active coverage session: {}", activeSessionId);
                return activeSessionId;
            }
            throw exception;
        }
    }

    public boolean shouldStopOnTeardown() {
        return !joinedExistingSession;
    }

    public void startSession(String sessionId) {
        if (isSessionRunning(sessionId)) {
            logger.info("Coverage session {} already running; skipping start", sessionId);
            return;
        }
        postJson("/sessions/" + sessionId + "/start", "{}", sessionId);
    }

    public boolean isSessionRunning(String sessionId) {
        try {
            String body = getJson("/sessions/" + sessionId + "/status");
            String status = extractStatus(body);
            return "Running".equalsIgnoreCase(status) || "3".equals(status);
        } catch (Exception exception) {
            logger.warn("Unable to read status for session {}", sessionId, exception);
            return false;
        }
    }

    public String stopSession(String sessionId) {
        HttpResponse<String> response = postJson("/sessions/" + sessionId + "/stop", "{}", sessionId);
        validateStopResponse(response.body());
        return response.body();
    }

    public void tcStart(String sessionId, String testCaseId, String description, String featurePath, String tagsJson) {
        String body = """
                {
                  "sessionId": "%s",
                  "testCaseId": "%s",
                  "description": %s,
                  "featurePath": %s,
                  "tags": %s,
                  "startedAtUtc": "%s"
                }
                """.formatted(
                escape(sessionId),
                escape(testCaseId),
                jsonString(description),
                jsonString(featurePath),
                tagsJson == null ? "[]" : tagsJson,
                Instant.now().toString());

        postJson("/coverage/tc/start", body, sessionId);
    }

    public void tcEnd(String sessionId, String testCaseId, String status) {
        String body = """
                {
                  "sessionId": "%s",
                  "testCaseId": "%s",
                  "status": "%s",
                  "finishedAtUtc": "%s"
                }
                """.formatted(
                escape(sessionId),
                escape(testCaseId),
                escape(status),
                Instant.now().toString());

        postJson("/coverage/tc/end", body, sessionId);
    }

    private void validateStopResponse(String responseBody) {
        if (!failOnMissingTcEnd) {
            return;
        }

        Matcher validMatcher = IS_VALID_PATTERN.matcher(responseBody);
        if (validMatcher.find() && "false".equals(validMatcher.group(1))) {
            Matcher missingMatcher = MISSING_ENDS_PATTERN.matcher(responseBody);
            String missingEnds = missingMatcher.find() ? missingMatcher.group(1).trim() : "";
            throw new IllegalStateException("Coverage session validation failed. missingEnds=" + missingEnds);
        }
    }

    private String getJson(String path) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + path))
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                    .build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new IllegalStateException("Coverage API " + path + " failed: " + response.statusCode() + " " + response.body());
            }
            return response.body();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Coverage API interrupted: " + path, exception);
        } catch (Exception exception) {
            throw new IllegalStateException("Coverage API error: " + path, exception);
        }
    }

    private static String extractActiveSessionId(Throwable exception) {
        Throwable current = exception;
        while (current != null) {
            String message = current.getMessage();
            if (message != null) {
                Matcher matcher = ACTIVE_SESSION_PATTERN.matcher(message);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
            current = current.getCause();
        }
        return null;
    }

    private static String extractSessionId(String responseBody) {
        Matcher matcher = SESSION_ID_PATTERN.matcher(responseBody);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalStateException("Unable to parse sessionId from coverage response: " + responseBody);
    }

    private static String extractStatus(String responseBody) {
        Matcher matcher = STATUS_PATTERN.matcher(responseBody);
        return matcher.find() ? matcher.group(2) : "";
    }

    private HttpResponse<String> postJson(String path, String body, String sessionId) {
        try {
            Duration timeout = path.contains("/start") || path.contains("/stop")
                    ? Duration.ofMinutes(8)
                    : Duration.ofMinutes(2);
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + path))
                    .timeout(timeout)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body));

            if (sessionId != null && !sessionId.isBlank()) {
                builder.header("X-Coverage-Session-Id", sessionId);
            }

            HttpResponse<String> response = HTTP_CLIENT.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new IllegalStateException("Coverage API " + path + " failed: " + response.statusCode() + " " + response.body());
            }

            logger.info("Coverage API {} -> {}", path, response.statusCode());
            return response;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Coverage API interrupted: " + path, exception);
        } catch (Exception exception) {
            throw new IllegalStateException("Coverage API error: " + path, exception);
        }
    }

    private static String trimTrailingSlash(String value) {
        if (value == null || value.isBlank()) {
            return "http://127.0.0.1:5055";
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String jsonString(String value) {
        if (value == null) {
            return "null";
        }
        return "\"" + escape(value) + "\"";
    }
}
