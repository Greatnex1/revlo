package com.nouah.revlo.validator;

import com.nouah.revlo.constants.ErrorMessages;
import io.micrometer.common.util.StringUtils;

import static com.nouah.revlo.constants.ErrorMessages.EMPTY_INPUT_ERROR;

public class InputValidator {

    public static void validateInput(String name, String field) {
        if (StringUtils.isEmpty(name) || org.apache.commons.lang3.StringUtils.isBlank(name)
                || StringUtils.isEmpty(name.trim()) || name.equals(ErrorMessages.UNDEFINED)) {
            throw new IllegalArgumentException(String.format(EMPTY_INPUT_ERROR, field));

        }
    }
}
