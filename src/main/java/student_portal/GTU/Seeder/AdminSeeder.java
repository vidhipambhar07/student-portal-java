package student_portal.GTU.Seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import student_portal.GTU.Enum.Role;
import student_portal.GTU.Model.User;
import student_portal.GTU.Repository.UserRepository;

@Component
public class AdminSeeder implements CommandLineRunner {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;



    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("admin@gtu.com")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@gtu.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setPhone("9999999999");

            userRepository.save(admin);
            System.out.println("✅ Admin user seeded successfully.");
        }
    }
}
