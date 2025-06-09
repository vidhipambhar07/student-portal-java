package student_portal.GTU.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import student_portal.GTU.Constants.ApiMessages;
import student_portal.GTU.Dto.*;
import student_portal.GTU.Enum.Role;
import student_portal.GTU.Model.User;
import student_portal.GTU.Service.JwtService;
import student_portal.GTU.Service.SemesterResultService;
import student_portal.GTU.Service.UserService;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @InjectMocks
    private StudentController studentController;

    @Mock
    private JwtService jwtService;

    @Mock
    private SemesterResultService semesterResultService;

    @Mock
    private UserService userService;


    private CreateStudentRequest createStudentRequest;
    private User mockUser;

    @BeforeEach
    void setUp() {
        CreateStudentRequest request = new CreateStudentRequest();
        request.setEmail("krina@yopmail.com");
        request.setUsername("krina");
        request.setPassword("12345678");

        User mockStudent = new User();
        mockStudent.setEmail("krina@yopmail.com");
        mockStudent.setUsername("krina");
        mockStudent.setRole(Role.STUDENT);

    }
    @Test
    void testCreateStudent_Success() {
        // Arrange
        CreateStudentRequest request = new CreateStudentRequest();
        request.setUsername("krina");
        request.setEmail("krina@yopmail.com");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("krina");
        mockUser.setEmail("krina@yopmail.com");
        when(userService.createStudent(request)).thenReturn(mockUser);
        // Act
        ResponseEntity<?> response = studentController.createStudent(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());

        SuccessResponse<?> successResponse = (SuccessResponse<?>) response.getBody();
        assertNotNull(successResponse);
        assertEquals(ApiMessages.STUDENT_CREATED, successResponse.getMessage());
        assertEquals(200, successResponse.getStatus());
        User returnedUser = (User) successResponse.getData();
        assertEquals("krina", returnedUser.getUsername());
        assertEquals("krina@yopmail.com", returnedUser.getEmail());
    }


    @Test
    void testUpdateProfile_Success() {
        // Arrange
        StudentProfileUpdateRequest request = new StudentProfileUpdateRequest();
        request.setUsername("krina");
        request.setPhone("1234567890");
        request.setOldPassword("oldpass");
        request.setNewPassword("newpass");

        String email = "krina@yopmail.com";
        Principal principal = () -> email;

        when(userService.updateStudentProfile(eq(email), any(StudentProfileUpdateRequest.class)))
                .thenReturn(true);

        // Act
        ResponseEntity<?> response = studentController.updateProfile(request, principal);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof SuccessResponse);
        SuccessResponse<?> success = (SuccessResponse<?>) response.getBody();
        assertEquals(200, success.getStatus());
        assertEquals("Profile updated successfully", success.getMessage()); // Or use ApiMessages.PROFILE_UPDATED
        verify(userService).updateStudentProfile(eq(email), any(StudentProfileUpdateRequest.class));
    }
    @Test
    void testUpdateProfile_Failure() {
        // Arrange
        StudentProfileUpdateRequest request = new StudentProfileUpdateRequest();
        request.setUsername("krina");
        request.setPhone("1234567890");

        String email = "krina@yopmail.com";
        Principal principal = () -> email;

        when(userService.updateStudentProfile(eq(email), any(StudentProfileUpdateRequest.class)))
                .thenReturn(false);

        // Act
        ResponseEntity<?> response = studentController.updateProfile(request, principal);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Profile update failed.", response.getBody());
        verify(userService).updateStudentProfile(eq(email), any(StudentProfileUpdateRequest.class));
    }
    @Test
    void testGetStudentResults_Admin_WithStudentId() {
        String email = "admin@example.com";
        Principal principal = () -> email;
        Long studentId = 1L;

        when(userService.getUserRoleByEmail(email)).thenReturn("ADMIN");

        List<SemesterGroupedResultDTO> mockResults = List.of(new SemesterGroupedResultDTO());
        when(semesterResultService.getResultsGroupedBySemester(studentId)).thenReturn(mockResults);

        ResponseEntity<List<SemesterGroupedResultDTO>> response =
                studentController.getStudentResults(principal, studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResults, response.getBody());
        verify(userService).getUserRoleByEmail(email);
        verify(semesterResultService).getResultsGroupedBySemester(studentId);
    }

    @Test
    void testGetStudentResults_Admin_WithoutStudentId() {
        String email = "admin@example.com";
        Principal principal = () -> email;

        when(userService.getUserRoleByEmail(email)).thenReturn("ADMIN");

        ResponseEntity<List<SemesterGroupedResultDTO>> response =
                studentController.getStudentResults(principal, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
    @Test
    void testGetStudentResults_Student_WithValidEmail() {
        String email = "student@example.com";
        Principal principal = () -> email;
        Long studentId = 10L;

        when(userService.getUserRoleByEmail(email)).thenReturn("STUDENT");
        when(userService.getUserIdByEmail(email)).thenReturn(studentId);

        List<SemesterGroupedResultDTO> mockResults = List.of(new SemesterGroupedResultDTO());
        when(semesterResultService.getResultsGroupedBySemester(studentId)).thenReturn(mockResults);

        ResponseEntity<List<SemesterGroupedResultDTO>> response =
                studentController.getStudentResults(principal, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResults, response.getBody());
    }
    @Test
    void testGetStudentResults_Student_EmailNotFound() {
        String email = "unknown@example.com";
        Principal principal = () -> email;

        when(userService.getUserRoleByEmail(email)).thenReturn("STUDENT");
        when(userService.getUserIdByEmail(email)).thenReturn(null); // not found

        ResponseEntity<List<SemesterGroupedResultDTO>> response =
                studentController.getStudentResults(principal, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
    @Test
    void testGetStudentRanking_AdminWithStudentId() {
        String email = "admin@example.com";
        Principal principal = () -> email;
        Long studentId = 1L;

        when(userService.getUserRoleByEmail(email)).thenReturn("ADMIN");

        List<SemesterRankingDTO> mockRankings = List.of(new SemesterRankingDTO());
        when(semesterResultService.calculateMeritRanking(studentId)).thenReturn(mockRankings);

        ResponseEntity<List<SemesterRankingDTO>> response = studentController.getStudentRanking(studentId, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRankings, response.getBody());
        verify(semesterResultService).calculateMeritRanking(studentId);
    }
    @Test
    void testGetStudentRanking_AdminWithoutStudentId() {
        String email = "admin@example.com";
        Principal principal = () -> email;

        when(userService.getUserRoleByEmail(email)).thenReturn("ADMIN");

        ResponseEntity<List<SemesterRankingDTO>> response = studentController.getStudentRanking(null, principal);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetStudentRanking_StudentWithAutoId() {
        String email = "student@example.com";
        Principal principal = () -> email;
        Long studentId = 2L;

        when(userService.getUserRoleByEmail(email)).thenReturn("STUDENT");
        when(userService.getUserIdByEmail(email)).thenReturn(studentId);

        List<SemesterRankingDTO> mockRankings = List.of(new SemesterRankingDTO());
        when(semesterResultService.calculateMeritRanking(studentId)).thenReturn(mockRankings);

        ResponseEntity<List<SemesterRankingDTO>> response = studentController.getStudentRanking(null, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockRankings, response.getBody());
        verify(semesterResultService).calculateMeritRanking(studentId);
    }

    @Test
    void testGetStudentRanking_StudentIdNotFound() {
        String email = "student@example.com";
        Principal principal = () -> email;

        when(userService.getUserRoleByEmail(email)).thenReturn("STUDENT");
        when(userService.getUserIdByEmail(email)).thenReturn(null); // no ID found

        ResponseEntity<List<SemesterRankingDTO>> response = studentController.getStudentRanking(null, principal);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
    @Test
    void testGetCgpaTrend_Success() {
        String email = "student@example.com";
        Principal principal = () -> email;
        Long studentId = 1L;

        List<CgpaTrendDTO> mockCgpaTrend = List.of(
                new CgpaTrendDTO( 1, 7.8),
                new CgpaTrendDTO( 2, 8.2)
        );

        when(userService.getUserIdByEmail(email)).thenReturn(studentId);
        when(semesterResultService.getCgpaTrend(studentId)).thenReturn(mockCgpaTrend);

        ResponseEntity<List<CgpaTrendDTO>> response = studentController.getCgpaTrend(principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCgpaTrend, response.getBody());
        verify(userService).getUserIdByEmail(email);
        verify(semesterResultService).getCgpaTrend(studentId);
    }
    @Test
    void testGetCgpaTrend_StudentIdNotFound() {
        String email = "student@example.com";
        Principal principal = () -> email;

        when(userService.getUserIdByEmail(email)).thenReturn(null);

        ResponseEntity<List<CgpaTrendDTO>> response = studentController.getCgpaTrend(principal);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).getUserIdByEmail(email);
        verify(semesterResultService, never()).getCgpaTrend(any());
    }
}