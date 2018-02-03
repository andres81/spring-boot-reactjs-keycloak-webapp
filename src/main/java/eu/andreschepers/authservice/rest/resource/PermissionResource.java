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
package eu.andreschepers.authservice.rest.resource;

import eu.andreschepers.authservice.domain.service.PermissionService;
import eu.andreschepers.authservice.rest.annotation.RestResourcePermission;
import eu.andreschepers.authservice.rest.annotation.Secure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/permission")
@Service
@Secure
public class PermissionResource {

    private PermissionService permissionService;

    @Autowired
    public PermissionResource(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    public Response getPermissions() {
        return Response.ok(permissionService.getPermissions()).build();
    }
}
