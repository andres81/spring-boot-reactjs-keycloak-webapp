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
package eu.andreschepers.authservice;

import eu.andreschepers.authservice.data.*;
import eu.andreschepers.authservice.data.repositories.*;
import eu.andreschepers.authservice.domain.bcrypt.PasswordEncoder;
import eu.andreschepers.authservice.domain.dto.SignupDto;
import eu.andreschepers.authservice.domain.exceptions.ServiceException;
import eu.andreschepers.authservice.domain.exceptions.ServiceExceptionType;
import eu.andreschepers.authservice.domain.interfaces.EmailService;
import eu.andreschepers.authservice.domain.service.DefaultUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.Validator;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by andres81 on 3/28/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultUserAccountServiceTest {

    @Mock private UserAccountRepository userAccountRepository;
    @Mock private UserTokenRepository userTokenRepository;
    @Mock private UserStatusRepository userStatusRepository;
    @Mock private UserPendingTokenRepository userPendingTokenRepository;
    @Mock private ResetPasswordTokenRepository resetPasswordTokenRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private Validator validator;
    @Mock private EmailService emailService;

    @InjectMocks
    private DefaultUserService userService;

    @Before
    public void init() {
        ReflectionTestUtils.setField(userService, "host", "http://host");
        ReflectionTestUtils.setField(userService, "port", "8080");
        ReflectionTestUtils.setField(userService, "pathResetPassword", "path");
        ReflectionTestUtils.setField(userService, "pathConfirmation", "path");
    }

    @Test
    public void userLogin() throws Exception {

        try {
            userService.userLogin(null, null);
            fail( "userLogin with null params not failing" );
        } catch (ServiceException ex) {
            assertTrue(ex.getType() == ServiceExceptionType.INVALID_ARGUMENTS_PROVIDED);
        }

        try {
            userService.userLogin("email", null);
            fail( "userLogin with null params not failing" );
        } catch (ServiceException ex) {
            assertTrue(ex.getType() == ServiceExceptionType.INVALID_ARGUMENTS_PROVIDED);
        }

        try {
            userService.userLogin(null, "password");
            fail( "userLogin with null params not failing" );
        } catch (ServiceException ex) {
            assertTrue(ex.getType() == ServiceExceptionType.INVALID_ARGUMENTS_PROVIDED);
        }

        when(userAccountRepository.findByEmail(any(String.class))).thenReturn(null);
        try {
            userService.userLogin("email", "password");
            fail( "No user found not failing" );
        } catch (ServiceException ex) {
            assertTrue(ex.getType() == ServiceExceptionType.INVALID_USERNAME_PASSWORD_COMBINATION);
        }
        reset(userAccountRepository);

        UserAccount user = new UserAccount();
        when(userAccountRepository.findByEmail(any(String.class))).thenReturn(user);
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(false);
        try {
            userService.userLogin("email", "password");
            fail( "Passwords dont match not failing" );
        } catch (ServiceException ex) {
            assertTrue(ex.getType() == ServiceExceptionType.INVALID_USERNAME_PASSWORD_COMBINATION);
        }
        reset(passwordEncoder);

        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
        UserStatus status = mock(UserStatus.class);
        when(userStatusRepository.findByUser(any(UserAccount.class))).thenReturn(status);
        try {
            userService.userLogin("email", "password");
            fail( "Status not active not failing" );
        } catch (ServiceException ex) {
            assertTrue(ex.getType() == ServiceExceptionType.LOGIN_WHILE_ACCOUNT_NOT_ACTIVE);
        }

        when(userTokenRepository.findByUser(any(UserAccount.class))).thenReturn(new UserToken());
        when(status.getAccountStatus()).thenReturn(AccountStatus.ACTIVE);
        userService.userLogin("email", "password");

        reset(userTokenRepository);
        assertTrue(userService.userLogin("email", "password") != null);
    }

    @Test
    public void userLogout() throws Exception {
        UserToken userToken = mock(UserToken.class);
        when(userTokenRepository.findByToken(anyString())).thenReturn(userToken);
        when(userToken.getUser()).thenReturn(new UserAccount());
        userService.userLogout("token");

        Mockito.verify(userTokenRepository, times(1)).deleteByUser(any(UserAccount.class));
    }

    @Test
    public void registerUser() throws Exception {

        SignupDto dto = new SignupDto();
        dto.setEmail("email");
        dto.setPassword("password");
        UserAccount user = mock(UserAccount.class);
        String email = "email";
        when(userAccountRepository.findByEmail(email)).thenReturn(user);
        when(validator.validate(dto)).thenReturn(new HashSet<>());
        try {
            userService.registerUser(dto);
            fail("No fail when email is already taken!");
        } catch(ServiceException ex) {
            assertTrue(ex.getType() == ServiceExceptionType.USERNAME_ALREADY_TAKEN);
        }
        Mockito.verify(userAccountRepository, times(1)).findByEmail(email);

        reset(userAccountRepository);

        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(user);
        try {
            userService.registerUser(dto);
        } catch(IllegalArgumentException ex) {}
        Mockito.verify(userAccountRepository, times(1)).save(any(UserAccount.class));
        Mockito.verify(userStatusRepository, times(1)).save(any(UserStatus.class));
    }

    @Test
    public void validateUserRegistration() throws Exception {

        String token = "token";
        when(userPendingTokenRepository.findByToken(token)).thenReturn(null);
        try {
            userService.validateUserRegistration(token);
            fail("No fail when email is already taken!");
        } catch(ServiceException ex) {
            assertTrue(ex.getType() == ServiceExceptionType.INVALID_ACCOUNT_VERIFICATION_TOKEN);
        }

        UserPendingToken pendingUserToken = mock(UserPendingToken.class);
        UserAccount user = mock(UserAccount.class);
        when(userPendingTokenRepository.findByToken(token)).thenReturn(pendingUserToken);
        when(pendingUserToken.getUser()).thenReturn(user);
        when(userStatusRepository.findByUser(user)).thenReturn(new UserStatus());
        userService.validateUserRegistration(token);
        Mockito.verify(userStatusRepository, times(1)).save(any(UserStatus.class));
        Mockito.verify(userPendingTokenRepository, times(1)).delete(anyLong());
    }

    @Test
    public void getNewAccountVerificationToken() throws Exception {

        String email = "email";
        UserAccount user = new UserAccount();
        when(userAccountRepository.findByEmail(email)).thenReturn(user);
        userService.getNewAccountVerificationToken(email);
        Mockito.verify(userPendingTokenRepository, times(1)).deleteByUser(user);
    }

    @Test
    public void isLoggedIn() throws Exception {
        assertFalse(userService.isLoggedIn(null));
        when(userTokenRepository.findByToken(anyString())).thenReturn(new UserToken());
        assertTrue(userService.isLoggedIn(null));
    }

    @Test
    public void resetPassword() throws Exception {

        String newPassword = "newPassword";
        String token = "token";
        try {
            userService.resetPassword(null, null);
            fail( "resetPassword with null params not failing" );
        } catch (ServiceException ex) {
            assertTrue(ex.getType() == ServiceExceptionType.INVALID_ARGUMENTS_PROVIDED);
        }

        try {
            userService.resetPassword(newPassword, null);
            fail( "resetPassword null params not failing" );
        } catch (ServiceException ex) {
            assertTrue(ex.getType() == ServiceExceptionType.INVALID_ARGUMENTS_PROVIDED);
        }

        try {
            userService.resetPassword(null, token);
            fail( "resetPassword with null params not failing" );
        } catch (ServiceException ex) {
            assertTrue(ex.getType() == ServiceExceptionType.INVALID_ARGUMENTS_PROVIDED);
        }

        try {
            userService.resetPassword(newPassword, token);
            fail( "resetPassword with null resetToken not failing" );
        } catch (ServiceException ex) {
            assertTrue(ex.getType() == ServiceExceptionType.INVALID_PASSWORD_RESET_TOKEN);
        }
        Mockito.verify(resetPasswordTokenRepository, times(1)).findByToken(token);

        ResetPasswordToken resetToken = mock(ResetPasswordToken.class);
        when(resetPasswordTokenRepository.findByToken(token)).thenReturn(resetToken);

        Date tokenCreationTime = Date.from(LocalDateTime.now().minusHours(1).minusMinutes(1).atZone(ZoneId.systemDefault()).toInstant());
        when(resetToken.getCreateDate()).thenReturn(tokenCreationTime);
        try {
            userService.resetPassword(newPassword, token);
            fail( "resetPassword with expired token not failing" );
        } catch (ServiceException ex) {
            assertTrue(ex.getType() == ServiceExceptionType.INVALID_PASSWORD_RESET_TOKEN);
        }

        tokenCreationTime = Date.from(LocalDateTime.now().minusHours(1).plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant());
        when(resetToken.getCreateDate()).thenReturn(tokenCreationTime);
        UserAccount user = mock(UserAccount.class);
        when(resetToken.getUser()).thenReturn(user);
        userService.resetPassword(newPassword, token);
    }

    @Test
    public void generateAndSendResetToken() throws Exception {

        String email = "email";
        userService.generateAndSendResetToken(email);
        Mockito.verify(resetPasswordTokenRepository, times(0)).findByUser(any(UserAccount.class));

        UserAccount user = mock(UserAccount.class);
        when(userAccountRepository.findByEmail(email)).thenReturn(user);
        userService.generateAndSendResetToken(email);
        Mockito.verify(resetPasswordTokenRepository, times(1)).findByUser(user);
    }

}