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

import eu.andreschepers.authservice.domain.bcrypt.BCryptPasswordEncoder;
import eu.andreschepers.authservice.domain.bcrypt.PasswordEncoder;
import eu.andreschepers.authservice.domain.interfaces.EmailService;
import eu.andreschepers.authservice.domain.service.DefaultEmailService;
import org.springframework.context.annotation.*;

import static org.mockito.Mockito.mock;

/**
 * Created by andres81 on 4/2/17.
 */
@Configuration
@Import(DataConfiguration.class)
@ComponentScan(
    basePackages = {"eu.andreschepers.authservice.domain"},
    excludeFilters= {
        @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=DefaultEmailService.class)
})
public class IntegrationTestEmbeddedDBConfiguration {

    @Bean
    public EmailService getEmailService() {
        return mock(EmailService.class);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}