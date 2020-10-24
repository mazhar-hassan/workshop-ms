package com.o4.microservices.common.utils;

import com.o4.microservices.common.exceptions.MissingRequiredFieldException;

/**
 * Copyright (c) 2019, Mazhar Hassan. All rights reserved.
 *
 * @author Mazhar Hassan
 * @project sample-api
 * @since Apr 15, 2019
 */
public interface ValidationUtils {

    public static void checkId(Integer id, String message) {
        if (ValueUtils.isEmpty(id)) {
            throw new MissingRequiredFieldException(message);
        }
    }

    public static void checkId(Long id, String message) {
        if (ValueUtils.isEmpty(id)) {
            throw new MissingRequiredFieldException(message);
        }
    }

    public static void checkId(Long id) {
        checkId(id, "Long value missing");
    }

    public static void checkString(String value) {
        checkString(value, "Missing require string value");
    }

    public static void checkString(String value, String message) {
        if (value == null || value.equals("")) {
            throw new MissingRequiredFieldException(message);
        }
    }

    public static void checkObject(Object obj) {
        if (obj == null) {
            throw new MissingRequiredFieldException("Required object is missing");
        }
    }

    public static void checkTrue(boolean value) {
        if (!value) throw new MissingRequiredFieldException("Condition is false");
    }

}
