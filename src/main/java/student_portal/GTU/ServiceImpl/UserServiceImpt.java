package student_portal.GTU.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import student_portal.GTU.Constants.ApiMessages;
import student_portal.GTU.Dto.*;
import student_portal.GTU.Enum.Role;
import student_portal.GTU.Model.User;
import student_portal.GTU.Repository.UserRepository;
import student_portal.GTU.Service.EmailService;
import student_portal.GTU.Service.UserService;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpt implements UserService {

    @Value("${app.reset-token.expiry-minutes}")
    private int resetTokenExpiryMinutes;
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    private JavaMailSender mailSender;
    private String generateEnrollmentNumber() {
        int currentYear = Year.now().getValue();  // e.g., 2025
        long count = userRepository.countByRole(Role.STUDENT) + 1; // total students + 1

        return String.format("GTU%d%04d", currentYear, count);
    }
    @Override
    public User createStudent(CreateStudentRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException(ApiMessages.EMAIL_ALREADY_EXIST);
        }
        User student = new User();
        student.setEmail(request.getEmail());
        student.setUsername(request.getUsername());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setRole(Role.STUDENT);
        String enrollmentNumber = generateEnrollmentNumber();
        student.setEnrollment_number(enrollmentNumber);
        student.setPhone(request.getPhone());

        return userRepository.save(student);

    }
        @Override
        public void generateResetToken(ForgotPasswordRequest request) {
            User user = null;
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                user = userRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new RuntimeException("User not found with email"));
            } else if (request.getEnrollmentNumber() != null && !request.getEnrollmentNumber().isEmpty()) {
                user = userRepository.findByEnrollmentNumber(request.getEnrollmentNumber())
                        .orElseThrow(() -> new RuntimeException("User not found with enrollment number"));
            } else {
                throw new RuntimeException("Email or Enrollment Number must be provided");
            }

            if (!user.getRole().name().equalsIgnoreCase(request.getRole())) {
                throw new RuntimeException("Role mismatch or unauthorized");
            }


            String token = UUID.randomUUID().toString();
            user.setResetToken(token);

            user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(resetTokenExpiryMinutes));
            userRepository.save(user);
            String resetLink = "http://localhost:8081/reset-password?token=" + token;
            emailService.sendResetEmail(user.getEmail(), user.getUsername(), resetLink);
        }

    @Override
    public boolean resetPassword(ResetPasswordRequest request) {
        Optional<User> userOptional = userRepository.findByResetToken(request.getToken());
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();
        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
        return true;
    }

    @Override
public boolean changePassword(String username, ChangePasswordRequest request) {
    Optional<User> optionalUser = userRepository.findByEmail(username);
    if (optionalUser.isEmpty()) {
        return false;
    }
    User user = optionalUser.get();
    if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
        return false;
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
    return true;
}
@Override
public boolean updateStudentProfile(String email, StudentProfileUpdateRequest request) {
    Optional<User> optionalUser = userRepository.findByEmail(email);
    if (optionalUser.isEmpty()) {
        return false;
    }
    User user = optionalUser.get();
    user.setUsername(request.getUsername());
    user.setPhone(request.getPhone());
    if (request.getOldPassword() != null && request.getNewPassword() != null) {
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    }
    userRepository.save(user);
    return true;
}
    @Override
    public Long getUserIdByEmail(String email) {
        return userRepository.findIdByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }


    @Override
    public String getUserRoleByEmail(String email) {
        return userRepository.findRoleByEmail(email);

    }

}


