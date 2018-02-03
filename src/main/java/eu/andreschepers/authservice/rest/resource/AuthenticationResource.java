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
package eu.andreschepers.authservice.rest.resource;

import eu.andreschepers.authservice.domain.dto.SignupDto;
import eu.andreschepers.authservice.domain.interfaces.UserService;
import eu.andreschepers.authservice.rest.pojo.LoginRequest;
import eu.andreschepers.authservice.rest.pojo.LoginResponse;
import eu.andreschepers.authservice.rest.pojo.ResetPasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import static eu.andreschepers.authservice.domain.interfaces.UserService.AUTHORIZATION_TOKEN;

/**
 *
 * @author Andre Schepers andreschepers81@gmail.com
 */
@Path("/auth")
@Service
public class AuthenticationResource {

    private final UserService userService;

    private boolean developmentMode;

    @Autowired
    public AuthenticationResource(
            @Value("${developer.mode}") boolean developmentMode,
            UserService userService) {
        this.developmentMode = developmentMode;
        this.userService = userService;
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest loginRequest) {
        String token = userService.userLogin(loginRequest.getUsername(), loginRequest.getPassword());

        String[] tokens = token.split(":");

        NewCookie authCookie = new NewCookie(
            AUTHORIZATION_TOKEN,
            tokens[0],
            "/",
            null,
            null,
            -1,
            !developmentMode,
            true);
        return Response
            .ok(new LoginResponse(tokens[1]), MediaType.APPLICATION_JSON)
            .cookie(authCookie)
            .build();
    }

    @GET
    @Path("/logout")
    public Response logout(
            @CookieParam(AUTHORIZATION_TOKEN) Cookie cookie) {
        Response.ResponseBuilder response = Response.ok("Successful logout!");
        if (cookie != null) {
            NewCookie authCookie = new NewCookie(
                    AUTHORIZATION_TOKEN,
                "",
                "/",
                null,
                null,
                -1,
                !developmentMode,
                true);
            response = response.cookie(authCookie);
        }
        return response.build();
    }

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    public void registerUser(SignupDto signupDto) {
        userService.registerUser(signupDto);
    }

    @PUT
    @Path("/userVerificationToken")
    public void getNewVerificationToken(@QueryParam("email") String email) {
        userService.getNewAccountVerificationToken(email);
    }

    @PUT
    @Path("/validateUserAccount")
    public void validateUserAccount(@QueryParam("token") String token) {
        userService.validateUserRegistration(token);
    }

    @PUT
    @Path("/resetpassword")
    public void generateResetPasswordToken(@QueryParam("email") String email) {
        userService.generateAndSendResetToken(email);
    }

    @POST
    @Path("/resetpassword")
    @Consumes(MediaType.APPLICATION_JSON)
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        userService.resetPassword(
            resetPasswordRequest.getNewPassword(),
            resetPasswordRequest.getResetToken());
    }

//    @GET
//    @Path("/QRCodeImageBase64")
//    public String getQRCodeImageBase64() {
//
//        Base32 base32 = new Base32();
//        String code = base32.encodeAsString("12345678901234567890".getBytes());
//        System.out.println(code);
//
//        // get QR stream from text using defaults
//        ByteArrayOutputStream stream = QRCode.from("otpauth://totp/Test:alice@google.com?secret=JBSWY3DPEHPK3PXP&issuer=Example").stream();
//
//        // supply own file name
//        String base64 = DatatypeConverter.printBase64Binary(stream.toByteArray());
//
//        return base64;
//    }
//
//    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, IOException {
//        String secretKeyString = getSecretKey();
//        System.err.println(secretKeyString);
//        int googleCode = getCode("L2ODBCC3OIS527FSQ5I6KZMOPUC6OGYKHCPZEMSVVB5QOFMT4DNYURCKTRW4VFATZWNKOOLKF54C3RKG6GHTKGVTGGLE4MENBZPLE6I=");
//        System.err.println(googleCode);
//
//        ByteArrayOutputStream stream = QRCode.from("otpauth://totp/Test:andreschepers81@gmail.com?secret=" + "L2ODBCC3OIS527FSQ5I6KZMOPUC6OGYKHCPZEMSVVB5QOFMT4DNYURCKTRW4VFATZWNKOOLKF54C3RKG6GHTKGVTGGLE4MENBZPLE6I=" + "&issuer=Example").stream();
//        String QRCODE = DatatypeConverter.printBase64Binary(stream.toByteArray());
//        System.err.println(QRCODE);
//    }
//
//    public static int getCode(String secretKey) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
//        final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator();
//        final Date now = new Date();
//        totp.getAlgorithm();
//        Base32 base32 = new Base32();
//        byte[] secretKeyBytes = base32.decode(secretKey.getBytes());
//        return totp.generateOneTimePassword(new SecretKeySpec(secretKeyBytes,0, secretKeyBytes.length, "SHA1"), now);
//    }
//
//    public static String getSecretKey() throws NoSuchAlgorithmException {
//        final KeyGenerator keyGenerator = KeyGenerator.getInstance(HmacOneTimePasswordGenerator.HOTP_HMAC_ALGORITHM);
//        // SHA-1 and SHA-256 prefer 64-byte (512-bit) keys; SHA512 prefers 128-byte keys
//        keyGenerator.init(512);
//        Key secretKey = keyGenerator.generateKey();
//        Base32 base32 = new Base32();
//        return new String(base32.encode(secretKey.getEncoded()));
//    }
}
