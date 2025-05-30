package student_portal.GTU.Service;

import io.jsonwebtoken.io.IOException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendResetEmail(String toEmail, String userName, String resetLink) {
        String html = loadResetTemplate(userName, resetLink);
        sendHtmlEmail(toEmail, "Reset Password", html);
    }
    public void sendHtmlEmail(String toEmail, String subject, String htmlBody) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("vidhipambhar07@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML

            mailSender.send(mimeMessage);
            System.out.println("HTML email sent to " + toEmail);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
    public String loadResetTemplate(String name, String link) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/email/reset-password.html");
            byte[] bytes = resource.getInputStream().readAllBytes();
            String content = new String(bytes);
            content = content.replace("[[NAME]]", name);
            content = content.replace("[[RESET_LINK]]", link);
            return content;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template", e);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

}
