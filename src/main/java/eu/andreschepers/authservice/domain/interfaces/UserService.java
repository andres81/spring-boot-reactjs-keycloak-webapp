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
package eu.andreschepers.authservice.domain.interfaces;

import eu.andreschepers.authservice.domain.dto.PermissionDto;
import eu.andreschepers.authservice.domain.dto.SignupDto;
import eu.andreschepers.authservice.domain.dto.UserAccountDto;
import eu.andreschepers.authservice.domain.dto.UserPermissionsDto;

import java.util.List;

/**
 * Created by andres81 on 1/1/17.
 */
public interface UserService {

    String AUTHORIZATION_TOKEN = "AUTHORIZATION_TOKEN";

    String userLogin(String username, String password);

    void userLogout(String token);

    void registerUser(SignupDto signupDto);

    void validateUserRegistration(String token);

    void getNewAccountVerificationToken(String email);

    boolean isLoggedIn(String token);

    void resetPassword(String newPassword, String token);

    void generateAndSendResetToken(String email);

    UserAccountDto getUser(String token);

    UserAccountDto getUser(long userId);

    void updateUser(String email, SignupDto update);

    void updateUser(UserAccountDto userAccountDto);

    void deleteUser(String email);

    void deleteUser(long userId);

    List<PermissionDto> getUserPermissions(long userId);

    List<UserAccountDto> getUsers();

    void saveUserPermissions(long userId, UserPermissionsDto userPermissionsDto);
}
