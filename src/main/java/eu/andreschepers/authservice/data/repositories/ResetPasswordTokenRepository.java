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
package eu.andreschepers.authservice.data.repositories;

import eu.andreschepers.authservice.data.ResetPasswordToken;
import eu.andreschepers.authservice.data.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by andres81 on 1/3/17.
 */
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {

    ResetPasswordToken findByToken(String token);
    ResetPasswordToken findByUser(UserAccount user);
    Long deleteByUser(UserAccount user);
    Long deleteByUserEmail(String email);
    void deleteByUserId(long userId);
}
