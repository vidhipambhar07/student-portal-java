package student_portal.GTU.Service;

import student_portal.GTU.Dto.AttendanceRankDTO;
import student_portal.GTU.Dto.AttendanceRequestDTO;
import student_portal.GTU.Dto.AttendanceSummaryDTO;
import student_portal.GTU.Model.Attendance;

import java.util.List;

public interface AttendanceService {
    Attendance markAttendance(AttendanceRequestDTO dto);
    List<AttendanceSummaryDTO> getMonthlyAttendanceSummary(int month, int year);
    List<AttendanceRankDTO> getMonthlyAttendanceRank(int month, int year);
}
