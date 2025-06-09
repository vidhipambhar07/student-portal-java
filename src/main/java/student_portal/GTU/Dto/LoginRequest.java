package student_portal.GTU.Dto;

public class LoginRequest {
    private String loginId; // Can be email or enrollment number
//    private String email;
    private String role;
    private String password;
    public LoginRequest(String loginId, String role, String password) {
        this.loginId = loginId;
//        this.email = email;
        this.role = role;
        this.password = password;
    }

    public LoginRequest() {

    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

//    private String password;

}

