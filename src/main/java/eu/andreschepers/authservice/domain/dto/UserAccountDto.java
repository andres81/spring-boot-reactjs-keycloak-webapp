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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andres81 on 1/1/17.
 */
public class UserAccountDto {

    private long id;
    private String fullName;
    private String email;
    private boolean isAdmin;
    private List<PermissionDto> permissions;

    public UserAccountDto(long id, String fullName, String email, boolean isAdmin, List<PermissionDto> permissions) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.isAdmin = isAdmin;
        this.permissions = new ArrayList<>(permissions);
    }

    public long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public List<PermissionDto> getPermissions() {
        return permissions;
    }
}
