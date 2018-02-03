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
package eu.andreschepers.authservice.rest.dynamic_feature;

import eu.andreschepers.authservice.rest.UserPrincipal;
import eu.andreschepers.authservice.rest.annotation.RestResourcePermission;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

@Component
public class AuthorizationFeature implements DynamicFeature {

    public void configure(ResourceInfo ri, FeatureContext ctx) {
        RestResourcePermission methodAuth = ri.getResourceMethod().getAnnotation(RestResourcePermission.class);
        if (methodAuth == null) {
            return;
        }
        ctx.register(new AuthPermissionFilter(methodAuth.name()));
    }

    @Priority(Priorities.AUTHORIZATION+1)
    private class AuthPermissionFilter implements ContainerRequestFilter {

        private String permissionName;

        public AuthPermissionFilter(String permissionName) {
            this.permissionName = permissionName;
        }

        @Override
        public void filter(ContainerRequestContext ctx) {
            UserPrincipal user = (UserPrincipal) ctx.getSecurityContext().getUserPrincipal();
            user.getPermissions();
            if (!user.isAdmin() && !user.getPermissions().contains(permissionName)) {
                throw new NotAuthorizedException("You don't have permission to access this resource");
            }
        }
    }
}