package com.nopcommerce.automation.utils;

import com.nopcommerce.automation.config.ConfigReader;
import com.nopcommerce.automation.constants.Constants;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public final class BackgroundTaskHttpHelper {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .followRedirects(HttpClient.Redirect.NEVER)
            .build();

    private BackgroundTaskHttpHelper() {
    }

    public static int postScheduleTask(String taskType) {
        try {
            String encodedTaskType = URLEncoder.encode(taskType, StandardCharsets.UTF_8);
            String body = "taskType=" + encodedTaskType;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ConfigReader.getBaseUrl() + Constants.SCHEDULE_TASK_RUN_PATH))
                    .timeout(Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<Void> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to POST schedule task: " + taskType, exception);
        }
    }

    public static int postScheduleTaskWithoutTaskType() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ConfigReader.getBaseUrl() + Constants.SCHEDULE_TASK_RUN_PATH))
                    .timeout(Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<Void> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to POST schedule task without task type", exception);
        }
    }

    public static int getScheduleTaskEndpoint() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ConfigReader.getBaseUrl() + Constants.SCHEDULE_TASK_RUN_PATH))
                    .timeout(Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()))
                    .GET()
                    .build();
            HttpResponse<Void> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to GET schedule task endpoint", exception);
        }
    }
}
