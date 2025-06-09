package student_portal.GTU.Controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;
import student_portal.GTU.Constants.ApiMessages;
import student_portal.GTU.Dto.*;
import student_portal.GTU.Model.User;
import student_portal.GTU.Service.JwtService;
import student_portal.GTU.Service.SemesterResultService;
import student_portal.GTU.Service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/student")
@Tag(name = "Student Management", description = "API for Admin to create student accounts")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private SemesterResultService semesterResultService;
    @Autowired
    private JwtService jwtService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody CreateStudentRequest request) {
        User createdStudent = userService.createStudent(request);
        return ResponseEntity.ok(new SuccessResponse<>(createdStudent, 200, ApiMessages.STUDENT_CREATED));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody StudentProfileUpdateRequest request, Principal principal) {
        boolean updated = userService.updateStudentProfile(principal.getName(), request);
        System.out.println(principal.getName());
        if (updated) {
            return ResponseEntity.ok(new SuccessResponse<>(null,200,ApiMessages.PROFILE_UPDATED));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Profile update failed.");
        }
    }
    @GetMapping("/results")
    public ResponseEntity<List<SemesterGroupedResultDTO>> getStudentResults(Principal principal, @RequestParam(required = false) Long studentId) {
        // 1. Get logged-in user email
        String email = principal.getName();

        // 2. Get user role
        String role = userService.getUserRoleByEmail(email);
        Long effectiveStudentId;
        if ("ADMIN".equalsIgnoreCase(role)) {
            // Admin must provide studentId as request param
            if (studentId == null) {
                return ResponseEntity.badRequest().body(null); // or return a message: "studentId param required"
            }
            effectiveStudentId = studentId;
        } else {
            // Normal student: get their own ID by email
            effectiveStudentId = userService.getUserIdByEmail(email);
            if (effectiveStudentId == null) {
                return ResponseEntity.badRequest().body(null);
            }
        }
        // Fetch results for the effective studentId
        List<SemesterGroupedResultDTO> results = semesterResultService.getResultsGroupedBySemester(effectiveStudentId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<SemesterRankingDTO>> getStudentRanking(
            @RequestParam(required = false) Long studentId,
            Principal principal) {

        String email = principal.getName();
        String role = userService.getUserRoleByEmail(email);

        if ("STUDENT".equalsIgnoreCase(role)) {
            studentId = userService.getUserIdByEmail(email);
        }

        if (studentId == null) {
            return ResponseEntity.badRequest().build();
        }

        List<SemesterRankingDTO> rankings = semesterResultService.calculateMeritRanking(studentId);
        return ResponseEntity.ok(rankings);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/cgpa-trend")
    public ResponseEntity<List<CgpaTrendDTO>> getCgpaTrend(Principal principal) {
        String email = principal.getName();
        Long studentId = userService.getUserIdByEmail(email);
        if (studentId == null) return ResponseEntity.badRequest().build();
        List<CgpaTrendDTO> cgpaTrend = semesterResultService.getCgpaTrend(studentId);
       return ResponseEntity.ok(cgpaTrend);
    }

}
