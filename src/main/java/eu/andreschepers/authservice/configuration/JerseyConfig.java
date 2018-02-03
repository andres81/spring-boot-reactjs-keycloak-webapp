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
package eu.andreschepers.authservice.configuration;

import eu.andreschepers.authservice.rest.ServiceExceptionToResponseMapper;
import eu.andreschepers.authservice.rest.dynamic_feature.AuthorizationFeature;
import eu.andreschepers.authservice.rest.dynamic_feature.BearerAuthFeature;
import eu.andreschepers.authservice.rest.filter.CORSFilter;
import eu.andreschepers.authservice.rest.resource.AuthenticationResource;
import eu.andreschepers.authservice.rest.resource.PermissionResource;
import eu.andreschepers.authservice.rest.resource.UserResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

/**
 * Created by andres81 on 3/25/17.
 */
@Component
@ApplicationPath("/rest")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(RolesAllowedDynamicFeature.class);
        register(UserResource.class);
        register(AuthenticationResource.class);
        register(ServiceExceptionToResponseMapper.class);
        register(CORSFilter.class);
        register(BearerAuthFeature.class);
        register(AuthorizationFeature.class);
        register(PermissionResource.class);
    }
}