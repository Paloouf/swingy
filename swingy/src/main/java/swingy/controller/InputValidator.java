package swingy.controller;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class InputValidator {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    public static String validateStringInput(String input) {
        UserInputDTO<String> dto = new UserInputDTO<>();
        dto.setStringInput(input);

        Set<ConstraintViolation<UserInputDTO<String>>> violations = validator.validate(dto);

        if (violations.isEmpty()) {
            return input;
        } else {
            for (ConstraintViolation<UserInputDTO<String>> violation : violations) {
                System.out.println(violation.getMessage());
            }
        }
        return null;
    }

	public static Integer validateIntegerInput(Integer input) {
        UserInputDTO<Integer> dto = new UserInputDTO<>();
        dto.setIntegerInput(input);

        Set<ConstraintViolation<UserInputDTO<Integer>>> violations = validator.validate(dto);

        if (violations.isEmpty()) {
            return input;
        } else {
            for (ConstraintViolation<UserInputDTO<Integer>> violation : violations) {
                System.out.println(violation.getMessage());
            }
        }
        return null;
    }
}
