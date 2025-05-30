package student_portal.GTU.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import student_portal.GTU.Constants.ApiMessages;
import student_portal.GTU.Dto.*;
import student_portal.GTU.Model.Attendance;
import student_portal.GTU.Service.AttendanceService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<?> markAttendance(@RequestBody AttendanceRequestDTO dto) {
        Attendance attendance = attendanceService.markAttendance(dto);
        return ResponseEntity.ok(new SuccessResponse<>(attendance,200, ApiMessages.ATTENDANCE_MARKED));
    }
    @GetMapping("/attendance/monthly-summary")
    public ResponseEntity<SuccessResponse<List<AttendanceSummaryDTO>>> getAttendanceSummary(
            @RequestParam int month,
            @RequestParam int year) {
        List<AttendanceSummaryDTO> summary = attendanceService.getMonthlyAttendanceSummary(month, year);
        return ResponseEntity.ok(new SuccessResponse<>(summary, 200, ApiMessages.ATTENDANCE_SUMMARY_GENERATED));
    }

    @GetMapping("/attendance/monthly-rank")
    public ResponseEntity<SuccessResponse<List<AttendanceRankDTO>>> getAttendanceRank(
            @RequestParam int month,
            @RequestParam int year) {
        List<AttendanceRankDTO> rankList = attendanceService.getMonthlyAttendanceRank(month, year);
        return ResponseEntity.ok(new SuccessResponse<>(rankList, 200, ApiMessages.ATTENDANCE_RANKING_GENERATED));
    }


}
