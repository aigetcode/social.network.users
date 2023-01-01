package com.social.network.users.util;

import com.social.network.users.dto.input.UserInput;
import com.social.network.users.entity.Country;
import com.social.network.users.entity.User;
import com.social.network.users.entity.UserSex;
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

    public static User createUser(UserInput userInput) {
        Country country = userInput.getCountry() == null ? null
                : new Country(userInput.getCountry());
        return new User(userInput.getVersion(), userInput.getName(), userInput.getSurname(),
                userInput.getLastName(), UserSex.valueOf(userInput.getSex()), userInput.getBirthdate(),
                country, userInput.getAvatar(), userInput.getUserDescription(), userInput.getNickname(),
                userInput.getEmail(), userInput.getPhoneNumber());
    }

}
