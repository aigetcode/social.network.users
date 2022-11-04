package com.social.network.users.util;

import org.apache.commons.validator.routines.EmailValidator;

public class Utils {

    private Utils() {
    }

    public static boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public static void required(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void verifyEmail(String email, String message) {
        if (!Utils.isValidEmail(email)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void state(boolean condition, String message) {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    }

}
