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
package eu.andreschepers.basewebapp.rest;

import eu.andreschepers.basewebapp.domain.exceptions.ServiceException;
import eu.andreschepers.basewebapp.rest.pojo.ServiceExceptionResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by andres81 on 3/27/17.
 */
@Provider
public class ServiceExceptionToResponseMapper implements ExceptionMapper<ServiceException> {

    @Override
    public Response toResponse(ServiceException e) {
        return Response.ok()
            .entity(new ServiceExceptionResponse(e))
            .type(MediaType.APPLICATION_JSON)
            .build();
    }
}
