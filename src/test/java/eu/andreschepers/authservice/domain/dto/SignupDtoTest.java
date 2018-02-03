/*
	Copyright 2018 Andre Schepers

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package eu.andreschepers.authservice.domain.dto;

import eu.andreschepers.authservice.domain.dto.validation.group.UpdateSignupGroup;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class SignupDtoTest {

    private Validator validator;

    @Before
    public void before() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidDtoNoGroups() {
        Set<ConstraintViolation<SignupDto>> violations = validator.validate(getValidDto());
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testValidDtoUpdateSignupGroup() {
        SignupDto dto = getValidDtoForUpdate();
        Set<ConstraintViolation<SignupDto>> violations = validator.validate(dto, UpdateSignupGroup.class);
        assertTrue(violations.isEmpty());
    }

    private SignupDto getValidDto() {
        SignupDto dto = new SignupDto();
        dto.setFullName("Andre Schepers");
        dto.setTermsagreed(true);
        dto.setEmail("andreschepers81@gmail.com");
        dto.setPassword("password");
        dto.setRepassword("password");
        return dto;
    }

    private SignupDto getValidDtoForUpdate() {
        SignupDto dto = new SignupDto();
        return dto;
    }
}