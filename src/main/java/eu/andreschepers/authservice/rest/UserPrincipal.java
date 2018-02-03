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
package eu.andreschepers.authservice.rest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class UserPrincipal implements Principal {

    private String email;
    private List<String> permissions = new ArrayList<>();
    private boolean isAdmin;
    private String token;

    public UserPrincipal(String email, boolean isAdmin, String token, List<String> permissions) {
        this.email = email;
        this.isAdmin = isAdmin;
        this.token = token;
        if (permissions != null) {
            this.permissions = permissions;
        }
    }

    @Override
    public String getName() {
        return email;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getToken() {
        return token;
    }
}
