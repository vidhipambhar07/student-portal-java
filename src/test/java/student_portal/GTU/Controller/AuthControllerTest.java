package student_portal.GTU.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import student_portal.GTU.Config.UserDetailsImpl;
import student_portal.GTU.Constants.ApiMessages;
import student_portal.GTU.Dto.*;
import student_portal.GTU.Enum.Role;
import student_portal.GTU.Model.RefreshToken;
import student_portal.GTU.Model.User;
import student_portal.GTU.Repository.UserRepository;
import student_portal.GTU.Service.JwtService;
import student_portal.GTU.Service.RefreshTokenService;
import student_portal.GTU.Service.UserService;
import java.security.Principal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserService userService;

    private LoginRequest loginRequest;
    private User mockUser;
    private RefreshTokenRequest request;
    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setLoginId("krina@yopmail.com");
        loginRequest.setPassword("12345678");
        loginRequest.setRole("STUDENT");

        mockUser = new User();
        mockUser.setEmail("krina@yopmail.com");
        mockUser.setUsername("krina");
        mockUser.setPassword("12345678");
        mockUser.setRole(Role.STUDENT);

        request = new RefreshTokenRequest();
        request.setRefreshToken("7d1fd521-3f2f-45c0-a2be-18d74fbdb8f6");

        refreshToken = new RefreshToken();
        refreshToken.setToken("7d1fd521-3f2f-45c0-a2be-18d74fbdb8f6");
        refreshToken.setUser(mockUser);

    }
    @Test
    void testLoginSuccess() {
        when(userRepository.findByEmail("krina@yopmail.com")).thenReturn(Optional.of(mockUser));
        when(authManager.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("krina@yopmail.com", "12345678"));

        UserDetailsImpl userDetails = new UserDetailsImpl(mockUser);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiU1RVREVOVCIsInN1YiI6ImtyaW5hQHlvcG1haWwuY29tIiwiaWF0IjoxNzQ4ODQ4MjA4LCJleHAiOjE3NDg4NDkxMDh9.FtzEizlgThtaNKVSN9iB1Adw6tHtfwh-PbWmn_D7mGM");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("7d1fd521-3f2f-45c0-a2be-18d74fbdb8f6");
        when(refreshTokenService.createOrUpdateRefreshToken(mockUser)).thenReturn(refreshToken);

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(200, response.getStatusCodeValue());

        SuccessResponse<?> body = (SuccessResponse<?>) response.getBody();
        AuthResponse authResponse = (AuthResponse) body.getData();

        assertEquals("eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiU1RVREVOVCIsInN1YiI6ImtyaW5hQHlvcG1haWwuY29tIiwiaWF0IjoxNzQ4ODQ4MjA4LCJleHAiOjE3NDg4NDkxMDh9.FtzEizlgThtaNKVSN9iB1Adw6tHtfwh-PbWmn_D7mGM", authResponse.getAccessToken());
        assertEquals("7d1fd521-3f2f-45c0-a2be-18d74fbdb8f6", authResponse.getRefreshToken());
        assertEquals("krina", authResponse.getUsername());
        assertEquals("krina@yopmail.com", authResponse.getEmail());
        assertEquals("STUDENT", authResponse.getRole());
    }

    @Test
    void testRefreshTokenSuccess() {
        when(refreshTokenService.findByToken("7d1fd521-3f2f-45c0-a2be-18d74fbdb8f6"))
                .thenReturn(Optional.of(refreshToken));


        when(jwtService.generateToken(any(UserDetails.class)))
                .thenReturn("eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiU1RVREVOVCIsInN1YiI6ImtyaW5hQHlvcG1haWwuY29tIiwiaWF0IjoxNzQ4ODU0MTE0LCJleHAiOjE3NDg4NTUwMTR9.12CFf-viCtguRcn_YRMmCe6DnuW2gZsg-vPh10K5xI0");

        ResponseEntity<SuccessResponse<AuthResponse>> response =
                authController.refreshToken(request);

        assertEquals(200, response.getStatusCodeValue());

        SuccessResponse<AuthResponse> body = response.getBody();
        assertNotNull(body);

        AuthResponse authResponse = body.getData();
        assertNotNull(authResponse);
        assertEquals("eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiU1RVREVOVCIsInN1YiI6ImtyaW5hQHlvcG1haWwuY29tIiwiaWF0IjoxNzQ4ODU0MTE0LCJleHAiOjE3NDg4NTUwMTR9.12CFf-viCtguRcn_YRMmCe6DnuW2gZsg-vPh10K5xI0", authResponse.getAccessToken());
        assertEquals("7d1fd521-3f2f-45c0-a2be-18d74fbdb8f6", authResponse.getRefreshToken());
        assertEquals("krina", authResponse.getUsername());
        assertEquals("krina@yopmail.com", authResponse.getEmail());
        assertEquals("STUDENT", authResponse.getRole());
    }

    @Test
    void testForgotPassword() {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("krina@yopmail.com");

        ResponseEntity<?> response = authController.forgotPassword(request);

        assertEquals(200, response.getStatusCodeValue());

        SuccessResponse<?> body = (SuccessResponse<?>) response.getBody();
        assertNotNull(body);
        assertEquals(200, body.getStatus());
        assertEquals(ApiMessages.RESET_LINK, body.getMessage());

        verify(userService, times(1)).generateResetToken(request);
    }

    @Test
    void testChangePasswordSuccess() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("12345678");
        request.setNewPassword("123456");

        Principal principal = () -> "krina@example.com"; // mock Principal with username/email

        when(userService.changePassword(principal.getName(), request)).thenReturn(true);


        ResponseEntity<?> response = authController.changePassword(request, principal);
        assertEquals(200, response.getStatusCodeValue());
        SuccessResponse<?> body = (SuccessResponse<?>) response.getBody();
        assertNotNull(body);
        assertEquals(200, body.getStatus());
        assertEquals(ApiMessages.PASSWORD_CHANGED, body.getMessage());

        verify(userService, times(1)).changePassword(principal.getName(), request);
    }

    @Test
    void testChangePasswordFailure() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("1234");
        request.setNewPassword("12345678");

        Principal principal = () -> "krina@yopmail.com";

        when(userService.changePassword(principal.getName(), request)).thenReturn(false);

        ResponseEntity<?> response = authController.changePassword(request, principal);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(ApiMessages.INCORRECT_PASSWORD, response.getBody());

        verify(userService, times(1)).changePassword(principal.getName(), request);
    }

    @Test
    void testLoginWithStudentRoleEmailFound() {
        LoginRequest req = new LoginRequest();
        req.setRole("STUDENT");
        req.setLoginId("krina@yopmail.com");
        req.setPassword("12345678");

        User mockUser = new User();
        mockUser.setEmail("krina@yopmail.com");
        mockUser.setUsername("krina");
        mockUser.setRole(Role.STUDENT);

        when(userRepository.findByEmail("krina@yopmail.com")).thenReturn(Optional.of(mockUser));

        Authentication auth = mock(Authentication.class);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        UserDetailsImpl userDetails = new UserDetailsImpl(mockUser);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiU1RVREVOVCIsInN1YiI6ImtyaW5hQHlvcG1haWwuY29tIiwiaWF0IjoxNzQ4ODU0MTE0LCJleHAiOjE3NDg4NTUwMTR9.12CFf-viCtguRcn_YRMmCe6DnuW2gZsg-vPh10K5xI0");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("7d1fd521-3f2f-45c0-a2be-18d74fbdb8f6");
        refreshToken.setUser(mockUser);
        when(refreshTokenService.createOrUpdateRefreshToken(mockUser)).thenReturn(refreshToken);

        ResponseEntity<?> response = authController.login(req);

        verify(userRepository).findByEmail("krina@yopmail.com");

        verify(authManager).authenticate(argThat(argument -> {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) argument;
            return token.getPrincipal().equals("krina@yopmail.com") && token.getCredentials().equals("12345678");
        }));

        assertEquals(HttpStatus.OK, response.getStatusCode());

        SuccessResponse<?> successResponse = (SuccessResponse<?>) response.getBody();
        assertNotNull(successResponse);

        AuthResponse authResponse = (AuthResponse) successResponse.getData();
        assertNotNull(authResponse);

        assertEquals("eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiU1RVREVOVCIsInN1YiI6ImtyaW5hQHlvcG1haWwuY29tIiwiaWF0IjoxNzQ4ODU0MTE0LCJleHAiOjE3NDg4NTUwMTR9.12CFf-viCtguRcn_YRMmCe6DnuW2gZsg-vPh10K5xI0", authResponse.getAccessToken());
        assertEquals("7d1fd521-3f2f-45c0-a2be-18d74fbdb8f6", authResponse.getRefreshToken());
        assertEquals("krina", authResponse.getUsername());
        assertEquals("krina@yopmail.com", authResponse.getEmail());
        assertEquals("STUDENT", authResponse.getRole());

        assertEquals(200, successResponse.getStatus());
        assertEquals(ApiMessages.LOGIN_SUCCESS, successResponse.getMessage());
    }


}