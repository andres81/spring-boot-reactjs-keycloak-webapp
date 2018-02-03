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
package eu.andreschepers.authservice.domain.dto.mapper;

import eu.andreschepers.authservice.data.UserAccount;
import eu.andreschepers.authservice.domain.dto.PermissionDto;
import eu.andreschepers.authservice.domain.dto.SignupDto;
import eu.andreschepers.authservice.domain.dto.UserAccountDto;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserAccountMapper {

    public static UserAccount mapFromSignupDto(SignupDto dto) {
        if (dto == null) {
            return null;
        }
        UserAccount user = new UserAccount();
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        return user;
    }

    public static UserAccountDto mapToDto(UserAccount userAccount) {
        if (userAccount == null) {
            return null;
        }
        return new UserAccountDto(
                userAccount.getId(),
                userAccount.getFullName(),
                userAccount.getEmail(),
                userAccount.isAdmin(),
                new ArrayList<>(
                    userAccount.getPermissions()
                        .stream()
                        .map(permission -> new PermissionDto(permission.getName(), permission.getDescription()))
                        .collect(Collectors.toList())));
    }
}
