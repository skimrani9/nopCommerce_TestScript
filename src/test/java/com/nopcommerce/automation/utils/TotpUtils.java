package com.nopcommerce.automation.utils;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;

public final class TotpUtils {

    private static final TimeProvider TIME_PROVIDER = new SystemTimeProvider();
    private static final CodeGenerator CODE_GENERATOR = new DefaultCodeGenerator();

    private TotpUtils() {
    }

    public static String generateCode(String secretKey) {
        try {
            return CODE_GENERATOR.generate(secretKey, TIME_PROVIDER.getTime() / 30);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to generate TOTP code", exception);
        }
    }
}
