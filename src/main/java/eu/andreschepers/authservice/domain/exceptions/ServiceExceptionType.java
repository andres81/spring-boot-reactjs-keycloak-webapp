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
package eu.andreschepers.authservice.domain.exceptions;

public enum ServiceExceptionType {

    UNAUTHORISED(1000, "You are not authorised for this resource.")
    ,INVALID_PASSWORD_RESET_TOKEN(1001, "This password reset token is not valid.")
    ,INVALID_ACCOUNT_VERIFICATION_TOKEN(1002, "This account verification token is not valid.")
    ,LOGIN_WHILE_ACCOUNT_NOT_ACTIVE(1003, "This account is not active")
    ,INVALID_USERNAME_PASSWORD_COMBINATION(1004, "Invalid username or password.")
    ,USERNAME_ALREADY_TAKEN(1005, "A user with this email already exists.")
    ,INVALID_ARGUMENTS_PROVIDED(1006, "Invalid arguments were provided.")
    ,INVALID_EXERCISE_NAME(2000, "Creating exercise failed: name for exercise already taken.");

    private final int errorCode;
    private final String errorMessage;

    ServiceExceptionType(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
