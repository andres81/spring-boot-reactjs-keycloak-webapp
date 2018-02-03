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
package eu.andreschepers.authservice.domain.service;

import eu.andreschepers.authservice.domain.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Created by andres81 on 2/28/17.
 */
@Service
public class DefaultEmailService implements EmailService {

    private final JavaMailSender mailSender;
    private final TaskExecutor taskExecutor;
    private final String emailFrom;

    @Autowired
    public DefaultEmailService(
            JavaMailSender mailSender,
            TaskExecutor taskExecutor,
            @Value("${email.server.from}") String emailFrom) {
        this.mailSender = mailSender;
        this.taskExecutor = taskExecutor;
        this.emailFrom = emailFrom;
    }

    @Override
    public void sendEmail(String email, String subject, String body) {
        taskExecutor.execute(new SendEmailTask(email, subject, body, emailFrom));
    }

    private class SendEmailTask implements Runnable {

        private final String email;
        private final String subject;
        private final String body;
        private final String emailFrom;

        public SendEmailTask(
                String email,
                String subject,
                String body,
                String emailFrom) {
            this.email = email;
            this.subject = subject;
            this.body = body;
            this.emailFrom = emailFrom;
        }

        @Override
        public void run() {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(email);
            message.setSubject(subject);
            message.setText(body);

            try {
                mailSender.send(message);
            } catch(MailException ex) {

            }
        }
    }
}
