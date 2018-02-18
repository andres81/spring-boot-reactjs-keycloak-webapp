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
package eu.andreschepers.basewebapp;

import org.keycloak.jaxrs.JaxrsBearerTokenFilter;
import org.keycloak.jaxrs.JaxrsBearerTokenFilterImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.URL;

@SpringBootApplication
public class AuthenticationServiceRestApiApplication {

	@Bean
	public JaxrsBearerTokenFilter getJaxrsBearerTokenFilter() {
		JaxrsBearerTokenFilterImpl filter = new JaxrsBearerTokenFilterImpl();
		filter.setKeycloakConfigFile("classpath:keycloak.json");
		return filter;
	}

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationServiceRestApiApplication.class, args);
	}
}
