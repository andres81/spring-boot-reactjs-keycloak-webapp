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
package eu.andreschepers.authservice.rest.pojo;

import eu.andreschepers.authservice.domain.exceptions.ServiceException;
import eu.andreschepers.authservice.domain.exceptions.ServiceExceptionType;

/**
 * Created by andres81 on 4/2/17.
 */
public class ServiceExceptionResponse {

    private final String error;
    private final int errorCode;
    private final String description;

    public ServiceExceptionResponse(ServiceException ex) {
        ServiceExceptionType type = ex.getType();
        error = type.name();
        errorCode = type.getErrorCode();
        description = type.getErrorMessage();
    }

    public String getError() {
        return error;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return description;
    }
}

