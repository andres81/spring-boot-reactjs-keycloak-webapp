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

import eu.andreschepers.authservice.data.*;
import eu.andreschepers.authservice.data.repositories.*;
import eu.andreschepers.authservice.domain.bcrypt.PasswordEncoder;
import eu.andreschepers.authservice.domain.dto.PermissionDto;
import eu.andreschepers.authservice.domain.dto.SignupDto;
import eu.andreschepers.authservice.domain.dto.UserAccountDto;
import eu.andreschepers.authservice.domain.dto.UserPermissionsDto;
import eu.andreschepers.authservice.domain.dto.mapper.PermissionMapper;
import eu.andreschepers.authservice.domain.dto.mapper.UserAccountMapper;
import eu.andreschepers.authservice.domain.dto.validation.group.UpdateSignupGroup;
import eu.andreschepers.authservice.domain.exceptions.ServiceException;
import eu.andreschepers.authservice.domain.exceptions.ServiceExceptionType;
import eu.andreschepers.authservice.domain.interfaces.EmailService;
import eu.andreschepers.authservice.domain.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Validator;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static eu.andreschepers.authservice.domain.exceptions.ServiceExceptionType.*;

/**
 * Created by andres81 on 1/1/17.
 */
@Service
public class DefaultUserService implements UserService {

    private final EmailService emailService;
    private final UserAccountRepository userAccountRepository;
    private final UserTokenRepository userTokenRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserPendingTokenRepository userPendingTokenRepository;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    @Value("${host}") private String host;
    @Value("${port}") private String port;
    @Value("${resetlink.path}") private String pathResetPassword;
    @Value("${confirmsignup.path}") private String pathConfirmation;
    @Value("${confirmation.required}") private boolean useEmailConfirmation;

    @Autowired
    public DefaultUserService(
            EmailService emailService,
            UserAccountRepository userAccountRepository,
            UserTokenRepository userTokenRepository,
            UserStatusRepository userStatusRepository,
            UserPendingTokenRepository userPendingTokenRepository,
            ResetPasswordTokenRepository resetPasswordTokenRepository,
            PermissionRepository permissionRepository,
            PasswordEncoder passwordEncoder,
            Validator validator) {
        this.emailService = emailService;
        this.userAccountRepository = userAccountRepository;
        this.userTokenRepository = userTokenRepository;
        this.userStatusRepository = userStatusRepository;
        this.userPendingTokenRepository = userPendingTokenRepository;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    /**
     *
     * @param email
     * @param password
     * @return
     */
    @Override
    @Transactional
    public String userLogin(String email, String password) {

        if (email == null ||
                password == null)
            throw new ServiceException(INVALID_ARGUMENTS_PROVIDED);

        UserAccount user = userAccountRepository.findByEmail(email);
        if (user == null ||
                !passwordEncoder.matches(password, user.getPassword()))
            throw new ServiceException(INVALID_USERNAME_PASSWORD_COMBINATION);

        UserStatus status;
        if ((status = userStatusRepository.findByUser(user)) == null ||
                status.getAccountStatus() != AccountStatus.ACTIVE)
            throw new ServiceException(LOGIN_WHILE_ACCOUNT_NOT_ACTIVE);

        String token = String.join(":", UUID.randomUUID().toString(), UUID.randomUUID().toString());
        UserToken userToken = new UserToken(token, user);
        userTokenRepository.save(userToken);

        return token;
    }

    /**
     *
     * @param token
     */
    @Override
    @Transactional
    public void userLogout(String token) {
        UserToken userToken = userTokenRepository.findByToken(token);
        if (userToken != null)
            userTokenRepository.deleteByUser(userToken.getUser());
    }

    /**
     *
     * @param signupDto
     */
    @Override
    @Transactional
    public void registerUser(SignupDto signupDto) {

        if (!validator.validate(signupDto).isEmpty()) throw new ServiceException(INVALID_ARGUMENTS_PROVIDED);

        String email = signupDto.getEmail();
        if (userAccountRepository.findByEmail(signupDto.getEmail()) != null)
            throw new ServiceException(USERNAME_ALREADY_TAKEN);

        UserAccount user = UserAccountMapper.mapFromSignupDto(signupDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userAccountRepository.save(user);
        userStatusRepository.save(new UserStatus(user, useEmailConfirmation ? AccountStatus.PENDING : AccountStatus.ACTIVE));
        if (useEmailConfirmation)
            sendConfirmationLink(email, user);
    }

    /**
     *
     * @param token
     */
    @Override
    @Transactional
    public void validateUserRegistration(String token) {
        UserPendingToken pendingUserToken = userPendingTokenRepository.findByToken(token);
        if (pendingUserToken == null) throw new ServiceException(INVALID_ACCOUNT_VERIFICATION_TOKEN);
        UserStatus userStatus = userStatusRepository.findByUser(pendingUserToken.getUser());
        userStatus.setAccountStatus(AccountStatus.ACTIVE);
        userStatusRepository.save(userStatus);
        userPendingTokenRepository.delete(pendingUserToken.getId());
    }

    /**
     *
     * @param email
     */
    @Override
    @Transactional
    public void getNewAccountVerificationToken(String email) {
        UserAccount user;
        if ((user = userAccountRepository.findByEmail(email)) == null) return;
        userPendingTokenRepository.deleteByUser(user);
        sendConfirmationLink(email, user);
    }

    /**
     *
     * @param token
     * @return
     */
    @Override
    public boolean isLoggedIn(String token) {
        return userTokenRepository.findByToken(token) != null;
    }

    /**
     *
     * @param newPassword
     * @param token
     */
    @Override
    @Transactional
    public void resetPassword(String newPassword, String token) {

        if (newPassword == null ||
                token == null) {
            throw new ServiceException(INVALID_ARGUMENTS_PROVIDED);
        }

        ResetPasswordToken resetToken = resetPasswordTokenRepository.findByToken(token);
        if (resetToken == null) {
            throw new ServiceException(INVALID_PASSWORD_RESET_TOKEN);
        }

        Date createDateTime = resetToken.getCreateDate();
        LocalDateTime ldt = LocalDateTime.ofInstant(createDateTime.toInstant(), ZoneId.systemDefault());
        if (ldt.plusHours(1L).isBefore(LocalDateTime.now())) {
            throw new ServiceException(INVALID_PASSWORD_RESET_TOKEN);
        }

        UserAccount user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        user = userAccountRepository.save(user);
        resetPasswordTokenRepository.deleteByUser(user);
    }

    /**
     *
     * @param email
     */
    @Override
    @Transactional
    public void generateAndSendResetToken(String email) {

        UserAccount user;
        if ((user = userAccountRepository.findByEmail(email)) == null) return;

        ResetPasswordToken token;
        if ((token = resetPasswordTokenRepository.findByUser(user)) == null) {
            token = new ResetPasswordToken();
            token.setUser(user);
        }
        token.setToken(UUID.randomUUID().toString());
        token.setCreateDate(new Date());
        resetPasswordTokenRepository.save(token);

        String resetLink = getEmailLink(host,
                port,
                pathResetPassword,
                token.getToken());

        emailService.sendEmail(email,"Reset your password", resetLink);
    }

    @Override
    @Transactional
    public UserAccountDto getUser(String token) {
        UserToken dbToken = userTokenRepository.findByToken(token);
        if (dbToken == null) return null;
        UserAccount user = dbToken.getUser();
        return UserAccountMapper.mapToDto(user);
    }

    @Override
    @Transactional
    public UserAccountDto getUser(long userId) {
        UserAccount userAccount = userAccountRepository.findById(userId);
        return UserAccountMapper.mapToDto(userAccount);
    }

    @Override
    @Transactional
    public void updateUser(String email, SignupDto update) {
        if (!validator.validate(update, UpdateSignupGroup.class).isEmpty()) throw new ServiceException(INVALID_ARGUMENTS_PROVIDED);
        UserAccount user = userAccountRepository.findByEmail(email);
        String newEmail;
        if ((newEmail = update.getEmail()) != null) user.setEmail(newEmail);
        String fullName;
        if ((fullName = update.getFullName()) != null) user.setFullName(fullName);
        String password;
        if ((password = update.getPassword()) != null && password.equals(update.getRepassword())) user.setPassword(passwordEncoder.encode(password));
        userAccountRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(UserAccountDto userAccountDto) {
        UserAccount userAccount = userAccountRepository.findById(userAccountDto.getId());
        userAccount.setEmail(userAccountDto.getEmail());
        userAccount.setFullName(userAccountDto.getFullName());
        userAccountRepository.save(userAccount);
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        userTokenRepository.deleteByUserEmail(email);
        userStatusRepository.deleteByUserEmail(email);
        userPendingTokenRepository.deleteByUserEmail(email);
        resetPasswordTokenRepository.deleteByUserEmail(email);
        userAccountRepository.deleteByEmail(email);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        userTokenRepository.deleteByUserId(userId);
        userStatusRepository.deleteByUserId(userId);
        userPendingTokenRepository.deleteByUserId(userId);
        resetPasswordTokenRepository.deleteByUserId(userId);
        userAccountRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public List<PermissionDto> getUserPermissions(long userId) {
        UserAccount user = userAccountRepository.findOne(userId);
        if (user == null) {
            throw new ServiceException(ServiceExceptionType.INVALID_ARGUMENTS_PROVIDED);
        }

        if (user.isAdmin()) {
            return Arrays.asList(new PermissionDto[]{new PermissionDto("*", "is_admin")});
        }

        return user.getPermissions()
            .stream()
            .map(permission -> PermissionMapper.mapToDto(permission))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveUserPermissions(long userId, UserPermissionsDto userPermissionsDto) {
        UserAccount user = userAccountRepository.findOne(userId);
        if (user == null) {
            throw new ServiceException(ServiceExceptionType.INVALID_ARGUMENTS_PROVIDED);
        }

        if (user.isAdmin()) {
            return;
        }

        Map<String, Boolean> newPermissions = userPermissionsDto.getProperties();
        Set<Permission> permissions = user.getPermissions();
        user.setPermissions(permissions.stream()
            .filter(permission -> filterPermission(permission, newPermissions))
            .collect(Collectors.toSet()));

        for (Map.Entry<String, Boolean> entry : newPermissions.entrySet()) {
            if (entry.getValue()) {
                Permission permission = permissionRepository.findByName(entry.getKey());
                if (permission != null) {
                    user.getPermissions().add(permission);
                }
            }
        }
        userAccountRepository.save(user);
    }

    private boolean filterPermission(Permission permission, Map<String, Boolean> newPermissions) {
        Boolean hasPermission = newPermissions.get(permission.getName());
        return hasPermission == null ? false : hasPermission;
    }

    @Override
    @Transactional
    public List<UserAccountDto> getUsers() {
        return userAccountRepository.findAll().stream()
            .map(userAccount -> UserAccountMapper.mapToDto(userAccount))
            .collect(Collectors.toList());
    }

    private void sendConfirmationLink(String email, UserAccount user) {
        String token = UUID.randomUUID().toString();

        UserPendingToken userPendingToken;
        if ((userPendingToken = userPendingTokenRepository.findByUser(user)) == null) {
            userPendingToken = new UserPendingToken(user, token);
        } else {
            userPendingToken.setToken(token);
        }
        userPendingTokenRepository.save(userPendingToken);

        String confirmationLink = getEmailLink(host,
            port,
            pathConfirmation,
            token);

        emailService.sendEmail(email,"Confirm your signup with your domain", confirmationLink);
    }

    private String getEmailLink(
            String host,
            String port,
            String path,
            String queryValue) {
        return UriComponentsBuilder
            .fromHttpUrl(host)
            .port(port)
            .path(path)
            .queryParam("token", queryValue)
            .build()
            .toString();
    }
}
