package com.nopcommerce.automation.coverage;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Scenario;
import io.cucumber.java.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class CoverageSessionHooks {

    private static final Logger logger = LogManager.getLogger(CoverageSessionHooks.class);
    private static final Pattern TC_ID_PATTERN = Pattern.compile("@(TC-NC-T\\d+)");
    private static final CoverageSessionClient CLIENT = new CoverageSessionClient();

    private static String sessionId;
    private static final ThreadLocal<String> activeTestCaseId = new ThreadLocal<>();

    @BeforeAll
    public static void beforeAll() {
        if (!CLIENT.isEnabled()) {
            logger.info("Coverage hooks disabled");
            return;
        }

        try {
            sessionId = CLIENT.resolveSessionId();
            CLIENT.startSession(sessionId);
            logger.info("Coverage session ready: {}", sessionId);
        } catch (Exception exception) {
            logger.error("Coverage session setup failed; UI tests will continue without coverage", exception);
            sessionId = null;
        }
    }

    @AfterAll(order = 99)
    public static void afterAll() {
        if (!CLIENT.isEnabled() || sessionId == null || !CLIENT.shouldStopOnTeardown()) {
            if (sessionId != null && !CLIENT.shouldStopOnTeardown()) {
                logger.info("Leaving coverage session running: {}", sessionId);
            }
            return;
        }

        try {
            CLIENT.stopSession(sessionId);
            logger.info("Coverage session stopped: {}", sessionId);
        } catch (Exception exception) {
            logger.error("Failed to stop coverage session {} (continuing)", sessionId, exception);
        }
    }

    @Before(order = 5)
    public void beforeScenario(Scenario scenario) {
        if (!CLIENT.isEnabled() || sessionId == null) {
            return;
        }

        String testCaseId = resolveTestCaseId(scenario);
        if (testCaseId == null) {
            logger.warn("Scenario '{}' has no @TC-NC-T* tag; skipping tcStart", scenario.getName());
            return;
        }

        try {
            activeTestCaseId.set(testCaseId);
            CLIENT.tcStart(
                    sessionId,
                    testCaseId,
                    scenario.getName(),
                    scenario.getUri() == null ? null : scenario.getUri().toString(),
                    toTagsJson(scenario.getSourceTagNames()));
            logger.info("tcStart registered for {}", testCaseId);
        } catch (Exception exception) {
            activeTestCaseId.remove();
            logger.warn("tcStart failed for {}; scenario will still run", testCaseId, exception);
        }
    }

    @After(order = 0) // runs after MFA/CAPTCHA cleanup (@After order 1) before driver quit (-1)
    public void afterScenario(Scenario scenario) {
        if (!CLIENT.isEnabled() || sessionId == null) {
            return;
        }

        String testCaseId = activeTestCaseId.get();
        if (testCaseId == null) {
            return;
        }

        try {
            CLIENT.tcEnd(sessionId, testCaseId, mapStatus(scenario.getStatus()));
            logger.info("tcEnd completed for {} with status {}", testCaseId, scenario.getStatus());
        } catch (Exception exception) {
            logger.warn("tcEnd failed for {} with status {}", testCaseId, scenario.getStatus(), exception);
        } finally {
            activeTestCaseId.remove();
        }
    }

    private static String resolveTestCaseId(Scenario scenario) {
        for (String tag : scenario.getSourceTagNames()) {
            Matcher matcher = TC_ID_PATTERN.matcher(tag);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    private static String mapStatus(Status status) {
        if (status == Status.FAILED) {
            return "failed";
        }
        if (status == Status.SKIPPED || status == Status.PENDING || status == Status.AMBIGUOUS || status == Status.UNDEFINED) {
            return "skipped";
        }
        return "passed";
    }

    private static String toTagsJson(Iterable<String> tags) {
        List<String> values = new ArrayList<>();
        tags.forEach(tag -> values.add("\"" + tag.replace("\"", "\\\"") + "\""));
        return values.stream().collect(Collectors.joining(",", "[", "]"));
    }

}
