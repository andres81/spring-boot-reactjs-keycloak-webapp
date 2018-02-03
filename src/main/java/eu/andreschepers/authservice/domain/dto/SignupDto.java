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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.andreschepers.authservice.domain.dto.validation.EqualPasswords;
import eu.andreschepers.authservice.domain.dto.validation.ValidBoolean;
import eu.andreschepers.authservice.domain.dto.validation.group.UpdateSignupGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.groups.Default;

/**
 * Created by andres81 on 2/21/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualPasswords(groups = {Default.class, UpdateSignupGroup.class})
public class SignupDto {

    @NotEmpty
    private String fullName;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String repassword;

    @ValidBoolean
    private Boolean termsagreed;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public Boolean getTermsagreed() {
        return termsagreed;
    }

    public void setTermsagreed(Boolean termsagreed) {
        this.termsagreed = termsagreed;
    }
}
