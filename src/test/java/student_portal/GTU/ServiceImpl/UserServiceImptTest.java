package student_portal.GTU.ServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import student_portal.GTU.Constants.ApiMessages;
import student_portal.GTU.Dto.*;
import student_portal.GTU.Enum.Role;
import student_portal.GTU.Model.User;
import student_portal.GTU.Repository.UserRepository;
import student_portal.GTU.Service.EmailService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class UserServiceImptTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpt userService;

    @Mock
    private EmailService emailService;
    @Test
    void testCreateStudent_Success() {
        CreateStudentRequest request = new CreateStudentRequest();
        request.setEmail("krina@yopmail.com");
        request.setUsername("krina");
        request.setPassword("12345678");
        request.setPhone("1234567890");

        // Mock repository call - email does not exist
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Mock password encoding
        when(passwordEncoder.encode("12345678")).thenReturn("12345678");

        // Mock save
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(request.getEmail());
        savedUser.setUsername(request.getUsername());
        savedUser.setPassword("12345678");
        savedUser.setRole(Role.STUDENT);
        savedUser.setEnrollment_number("GTU20250003");
        savedUser.setPhone(request.getPhone());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Call the method
        User result = userService.createStudent(request);

        // Verify result
        assertNotNull(result);
        assertEquals("krina@yopmail.com", result.getEmail());
        assertEquals("krina", result.getUsername());
        assertEquals("12345678", result.getPassword());
        assertEquals(Role.STUDENT, result.getRole());
        assertEquals("1234567890", result.getPhone());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateStudent_EmailAlreadyExists() {
        CreateStudentRequest request = new CreateStudentRequest();
        request.setEmail("krina@yopmail.com");

        // Email already exists
        when(userRepository.findByEmail("krina@yopmail.com"))
                .thenReturn(Optional.of(new User()));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.createStudent(request));

        assertEquals(ApiMessages.EMAIL_ALREADY_EXIST, exception.getMessage());
    }

    @Test
    void testGetUserIdByEmail_Success() {
        String email = "krina@yopmail.com";
        Long expectedId = 4L;

        when(userRepository.findIdByEmail(email)).thenReturn(Optional.of(expectedId));

        Long result = userService.getUserIdByEmail(email);

        assertEquals(expectedId, result);
        verify(userRepository).findIdByEmail(email);
    }

    @Test
    void testGetUserIdByEmail_NotFound() {
        String email = "notfound@example.com";

        when(userRepository.findIdByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.getUserIdByEmail(email)
        );

        assertEquals("User not found with email: " + email, exception.getMessage());
        verify(userRepository).findIdByEmail(email);
    }

    @Test
    void testGetUserRoleByEmail() {
        String email = "krina@yopmail.com";
        String expectedRole = "STUDENT";

        when(userRepository.findRoleByEmail(email)).thenReturn(expectedRole);

        String result = userService.getUserRoleByEmail(email);

        assertEquals(expectedRole, result);
        verify(userRepository).findRoleByEmail(email);
    }


    @Test
    void testUpdateStudentProfile_UserNotFound() {
        String email = "unknown@example.com";
        StudentProfileUpdateRequest request = new StudentProfileUpdateRequest();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        boolean result = userService.updateStudentProfile(email, request);

        assertFalse(result);
        verify(userRepository).findByEmail(email);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateStudentProfile_SuccessWithoutPasswordChange() {
        String email = "krina@yopmail.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("12345678");

        StudentProfileUpdateRequest request = new StudentProfileUpdateRequest();
        request.setUsername("krina");
        request.setPhone("12345678");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean result = userService.updateStudentProfile(email, request);

        assertTrue(result);
        assertEquals("krina", user.getUsername());
        assertEquals("12345678", user.getPhone());

        verify(userRepository).save(user);
    }

    @Test
    void testUpdateStudentProfile_PasswordMismatch() {
        String email = "krina@yopmail.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("12345678");

        StudentProfileUpdateRequest request = new StudentProfileUpdateRequest();
        request.setUsername("krina");
        request.setPhone("1234567890");
        request.setOldPassword("123456");
        request.setNewPassword("12345678");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "12345678")).thenReturn(false);

        boolean result = userService.updateStudentProfile(email, request);

        assertFalse(result);
        verify(userRepository, never()).save(any());
    }
    @Test
    void testUpdateStudentProfile_PasswordChangeSuccess() {
        String email = "krina@yopmail.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("12345678");

        StudentProfileUpdateRequest request = new StudentProfileUpdateRequest();
        request.setUsername("krina");
        request.setPhone("1234567890");
        request.setOldPassword("12345678");
        request.setNewPassword("12456");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("12345678", "12345678")).thenReturn(true);
        when(passwordEncoder.encode("12456")).thenReturn("12456");

        boolean result = userService.updateStudentProfile(email, request);

        assertTrue(result);
        assertEquals("12456", user.getPassword());
        verify(userRepository).save(user);
    }
    @Test
    void testChangePassword_UserNotFound() {
        String username = "krina1@yopmail.com";
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("1234578");
        request.setNewPassword("123456");

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        boolean result = userService.changePassword(username, request);

        assertFalse(result);
        verify(userRepository).findByEmail(username);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(userRepository, never()).save(any());
    }
    @Test
    void testChangePassword_OldPasswordMismatch() {
        String username = "krina@yopmail.com";
        User user = new User();
        user.setPassword("12345");

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("12345");
        request.setNewPassword("12345678");

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("12345", "12345")).thenReturn(false);


        boolean result = userService.changePassword(username, request);

        assertFalse(result);
        verify(userRepository).findByEmail(username);
        verify(passwordEncoder).matches("12345", "12345");
        verify(userRepository, never()).save(any());
    }

    @Test
    void testChangePassword_Success() {
        String username = "krina@yopmail.com";
        User user = new User();
        user.setPassword("12345678");

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("123456");
        request.setNewPassword("12345678");

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "12345678")).thenReturn(true);
        when(passwordEncoder.encode("12345678")).thenReturn("encodedNewPassword");

        boolean result = userService.changePassword(username, request);

        assertTrue(result);
        assertEquals("encodedNewPassword", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void testResetPassword_TokenNotFound() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("invalid-token");
        request.setNewPassword("123456");

        when(userRepository.findByResetToken("invalid-token")).thenReturn(Optional.empty());

        boolean result = userService.resetPassword(request);

        assertFalse(result);
        verify(userRepository).findByResetToken("invalid-token");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testResetPassword_TokenExpired() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("valid-token");
        request.setNewPassword("123456");

        User user = new User();
        user.setResetTokenExpiry(LocalDateTime.now().minusMinutes(1)); // expired

        when(userRepository.findByResetToken("valid-token")).thenReturn(Optional.of(user));

        boolean result = userService.resetPassword(request);

        assertFalse(result);
        verify(userRepository).findByResetToken("valid-token");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }
    @Test
    void testResetPassword_Success() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("valid-token");
        request.setNewPassword("123456");

        User user = new User();
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(10)); // not expired
        user.setResetToken("valid-token");

        when(userRepository.findByResetToken("valid-token")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("123456")).thenReturn("encodedNewPass");

        boolean result = userService.resetPassword(request);

        assertTrue(result);
        assertEquals("encodedNewPass", user.getPassword());
        assertNull(user.getResetToken());
        assertNull(user.getResetTokenExpiry());

        verify(userRepository).save(user);
    }
    @Test
    void testGenerateResetToken_ByEmail_Success() {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("krina@yopmail.com");
        request.setRole("STUDENT");

        User user = new User();
        user.setEmail("krina@yopmail.com");
        user.setUsername("krina");
        user.setRole(Role.STUDENT);

        when(userRepository.findByEmail("krina@yopmail.com")).thenReturn(Optional.of(user));

        userService.generateResetToken(request);

        assertNotNull(user.getResetToken());
        assertNotNull(user.getResetTokenExpiry());
        verify(userRepository).save(user);
        verify(emailService).sendResetEmail(eq("krina@yopmail.com"), eq("krina"), contains(user.getResetToken()));
    }

    @Test
    void testGenerateResetToken_ByEnrollmentNumber_Success() {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEnrollmentNumber("ENR123");
        request.setRole("STUDENT");

        User user = new User();
        user.setEnrollment_number("ENR123");
        user.setUsername("krina");
        user.setRole(Role.STUDENT);

        when(userRepository.findByEnrollmentNumber("ENR123")).thenReturn(Optional.of(user));

        userService.generateResetToken(request);

        assertNotNull(user.getResetToken());
        assertNotNull(user.getResetTokenExpiry());
        verify(userRepository).save(user);
        verify(emailService).sendResetEmail(eq(user.getEmail()), eq("krina"), contains(user.getResetToken()));
    }

    @Test
    void testGenerateResetToken_MissingEmailAndEnrollmentNumber_Throws() {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail(null);
        request.setEnrollmentNumber(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.generateResetToken(request);
        });

        assertEquals("Email or Enrollment Number must be provided", ex.getMessage());
        verifyNoInteractions(userRepository);
        verifyNoInteractions(emailService);
    }

    @Test
    void testGenerateResetToken_RoleMismatch_Throws() {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("krina@yopmail.com");
        request.setRole("ADMIN");

        User user = new User();
        user.setEmail("krina@yopmail.com");
        user.setRole(Role.STUDENT); // different role

        when(userRepository.findByEmail("krina@yopmail.com")).thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.generateResetToken(request);
        });

        assertEquals("Role mismatch or unauthorized", ex.getMessage());
        verify(userRepository).findByEmail("krina@yopmail.com");
        verify(userRepository, never()).save(any());
        verifyNoInteractions(emailService);
    }
}