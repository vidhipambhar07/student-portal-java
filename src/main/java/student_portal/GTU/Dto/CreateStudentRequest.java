package student_portal.GTU.Dto;

public class CreateStudentRequest {
    private String email;
    private String username;
    private String password;
    private String enrollmentNumber;
    private String phone;
    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnrollment_number() {
        return enrollmentNumber;
    }

    public void setEnrollment_number(String enrollment_number) {
        this.enrollmentNumber = enrollment_number;
    }
}
