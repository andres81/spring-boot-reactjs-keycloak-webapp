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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ValidatorFactory;
import java.util.Properties;

/**
 *
 * @author Andre Schepers andreschepers81@gmail.com
 */
@Configuration
@ComponentScan({"eu.andreschepers.authservice"})
@Import(DataConfiguration.class)
public class ApplicationConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public ValidatorFactory validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TaskExecutor getTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(env.getProperty("email.corePoolSize", Integer.class));
        executor.setMaxPoolSize(env.getProperty("email.maxPoolSize", Integer.class));
        executor.setQueueCapacity(env.getProperty("email.queueCapacity", Integer.class));
        return executor;
    }

    @Bean
    public JavaMailSenderImpl getJavaMailSender() {

        JavaMailSenderImpl impl = new JavaMailSenderImpl();
        impl.setHost(env.getProperty("email.server.host"));
        impl.setUsername(env.getProperty("email.server.username"));
        impl.setPassword(env.getProperty("email.server.password"));
        impl.setPort(env.getProperty("email.server.port", Integer.class));
        impl.setProtocol(env.getProperty("email.server.protocol"));

        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol",env.getProperty("email.server.protocol"));
        properties.setProperty("mail.smtp.auth","true");
        properties.setProperty("mail.smtp.starttls.enable","true");
        properties.setProperty("mail.smtp.starttls.required", "true");
        properties.setProperty("mail.debug",env.getProperty("email.server.debug"));
        impl.setJavaMailProperties(properties);

        return impl;
    }
}
