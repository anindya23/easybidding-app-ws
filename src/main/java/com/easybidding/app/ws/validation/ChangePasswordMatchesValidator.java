package com.easybidding.app.ws.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.easybidding.app.ws.shared.dto.PasswordDto;

public class ChangePasswordMatchesValidator implements ConstraintValidator<ChangePasswordMatches, Object> {

	@Override
	public void initialize(final ChangePasswordMatches constraintAnnotation) {
		//
	}

	@Override
	public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
		final PasswordDto user = (PasswordDto) obj;
		return user.getPassword().equals(user.getMatchingPassword());
	}

}