package student_portal.GTU.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import student_portal.GTU.Constants.ApiMessages;
import student_portal.GTU.Dto.*;
import student_portal.GTU.Enum.Role;
import student_portal.GTU.Execption.NotFoundException;
import student_portal.GTU.Service.JwtService;
import student_portal.GTU.Model.RefreshToken;
import student_portal.GTU.Model.User;
import student_portal.GTU.Repository.UserRepository;
import student_portal.GTU.Service.RefreshTokenService;
import student_portal.GTU.Config.UserDetailsImpl;
import student_portal.GTU.Service.UserService;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    public AuthController(AuthenticationManager authManager, JwtService jwtService, UserRepository userRepository,RefreshTokenService refreshTokenService, UserService userService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    @Autowired
    private AuthenticationManager authManager;
    @Autowired private JwtService jwtService;
    @Autowired private UserRepository userRepository;
    @Autowired private RefreshTokenService refreshTokenService;
    @Autowired
    UserService userService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Step 1: Identify user
        Optional<User> userOpt;
        if (Role.valueOf(request.getRole().toUpperCase()) == Role.STUDENT) {
            userOpt = userRepository.findByEmail(request.getLoginId());
            if (userOpt.isEmpty()) {
                userOpt = userRepository.findByEnrollmentNumber(request.getLoginId());
            }
        } else {
            userOpt = userRepository.findByEmail(request.getLoginId());
        }

        User user = userOpt.orElseThrow(() ->
                new NotFoundException("User not found with identifier: " + request.getLoginId()));
        // Step 2: Authenticate with actual email
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), request.getPassword())
        );

        // Step 3: Generate JWT and refresh token
        UserDetails userDetails = new UserDetailsImpl(user);
        String jwt = jwtService.generateToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createOrUpdateRefreshToken(user);

        return ResponseEntity.ok(new SuccessResponse<>(new AuthResponse(
                jwt,
                refreshToken.getToken(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        ), 200, ApiMessages.LOGIN_SUCCESS));

    }

    @PostMapping("/refresh-token")
    public ResponseEntity<SuccessResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {

        String requestToken = request.getRefreshToken();

        // Find token or throw if not found
        RefreshToken refreshToken = refreshTokenService.findByToken(requestToken)
                .orElseThrow(() -> new RuntimeException(ApiMessages.REFRESH_TOKEN_NOT_FOUND));

        // Verify expiration or throw if expired
        refreshTokenService.verifyExpiration(refreshToken);

        // Get user from refresh token
        User user = refreshToken.getUser();

        // Create UserDetailsImpl for JWT creation
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        // Generate new access token
        String newAccessToken = jwtService.generateToken(userDetails);

        // Build auth response data
        AuthResponse authResponse = new AuthResponse(
                newAccessToken,
                requestToken,
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );

        // Wrap inside your success response format
        SuccessResponse<AuthResponse> successResponse = new SuccessResponse<>(
                authResponse,
                200,
                ApiMessages.REFRESH_TOKEN_SUCCESS
        );

        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        userService.generateResetToken(request);
        return ResponseEntity.ok(new SuccessResponse<>(null, 200, ApiMessages.RESET_LINK));
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean isReset = userService.resetPassword(request);
        if (isReset) {
            return ResponseEntity.ok(new SuccessResponse<>(null,200,ApiMessages.RESET_PASSWORD));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }
    }
    @PatchMapping("/change-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal principal) {
        boolean changed = userService.changePassword(principal.getName(), request);

        if (changed) {
            return ResponseEntity.ok(new SuccessResponse<>(null,200,ApiMessages.PASSWORD_CHANGED));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiMessages.INCORRECT_PASSWORD);
        }
    }


}
