package student_portal.GTU.Service;

import student_portal.GTU.Dto.*;
import student_portal.GTU.Model.User;


public interface UserService {
    User createStudent(CreateStudentRequest createStudentRequest);
    void generateResetToken(ForgotPasswordRequest request);
    boolean resetPassword(ResetPasswordRequest request);
    boolean changePassword(String principal,ChangePasswordRequest request);
    boolean updateStudentProfile(String principal, StudentProfileUpdateRequest request);
    Long getUserIdByEmail(String username);
    String getUserRoleByEmail(String email);
}
