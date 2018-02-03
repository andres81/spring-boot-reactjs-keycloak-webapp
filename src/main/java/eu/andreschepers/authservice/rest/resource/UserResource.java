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

import eu.andreschepers.authservice.domain.dto.SignupDto;
import eu.andreschepers.authservice.domain.dto.UserAccountDto;
import eu.andreschepers.authservice.domain.dto.UserPermissionsDto;
import eu.andreschepers.authservice.domain.interfaces.UserService;
import eu.andreschepers.authservice.rest.UserPrincipal;
import eu.andreschepers.authservice.rest.annotation.RestResourcePermission;
import eu.andreschepers.authservice.rest.annotation.Secure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/user")
@Service
@Secure
public class UserResource {

    private UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@Context SecurityContext context) {
        UserPrincipal details = (UserPrincipal) context.getUserPrincipal();
        return Response.ok(userService.getUser(details.getToken()), MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateUser(
            @Context SecurityContext context,
            SignupDto update) {
        userService.updateUser(context.getUserPrincipal().getName(), update);
    }

    @DELETE
    public void deleteUser(@Context SecurityContext context) {
        userService.deleteUser(context.getUserPrincipal().getName());
    }

    @GET
    @Path("/{userId}/permissions")
    @RolesAllowed("admin")
    public Response getUserPermissions(@PathParam("userId") long userId) {
        return Response.ok(userService.getUserPermissions(userId), MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("/{userId}/permissions")
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateUserPermissions(@PathParam("userId") long userId, UserPermissionsDto userPermissionsDto) {
        userService.saveUserPermissions(userId, userPermissionsDto);
    }

    @DELETE
    @Path("/{userId}")
    @RestResourcePermission(name="user_delete", description="Delete a user with a given user id")
    public void deleteUser(@PathParam("userId") long userId) {
        userService.deleteUser(userId);
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RestResourcePermission(name="user_get", description="Retrieve a users data")
    public Response getUser(@PathParam("userId") long userId) {
        return Response.ok(userService.getUser(userId), MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RestResourcePermission(name="user_update", description="Update a user's data")
    public void updateUser(
            @PathParam("userId") long userId,
            UserAccountDto userAccountDto) {
        if (userId != userAccountDto.getId()) {
            throw new WebApplicationException("User ids did not match");
        }
        userService.updateUser(userAccountDto);
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    @RestResourcePermission(name="user_list", description="Retrieve a list of all users")
    public Response getUser() {
        return Response.ok(userService.getUsers(), MediaType.APPLICATION_JSON).build();
    }
}