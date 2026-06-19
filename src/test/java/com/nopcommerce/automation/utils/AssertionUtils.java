package com.nopcommerce.automation.utils;

import org.assertj.core.api.Assertions;

public final class AssertionUtils {

    private AssertionUtils() {
    }

    public static void assertTrue(boolean condition, String message) {
        Assertions.assertThat(condition)
                .as(message)
                .isTrue();
    }

    public static void assertFalse(boolean condition, String message) {
        Assertions.assertThat(condition)
                .as(message)
                .isFalse();
    }

    public static void assertEquals(String actual, String expected, String message) {
        Assertions.assertThat(actual)
                .as(message)
                .isEqualTo(expected);
    }

    public static void assertContains(String actual, String expectedSubstring, String message) {
        Assertions.assertThat(actual)
                .as(message)
                .contains(expectedSubstring);
    }
}
