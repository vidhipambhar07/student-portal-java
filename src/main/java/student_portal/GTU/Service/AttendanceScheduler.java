package student_portal.GTU.Service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import student_portal.GTU.Dto.AttendanceRankDTO;
import student_portal.GTU.Dto.AttendanceSummaryDTO;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceScheduler {
    private final AttendanceService attendanceService;

    public AttendanceScheduler(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

//    @Scheduled(fixedDelay = 10000) // every 10 seconds (just for testing)
    @Scheduled(cron = "0 0 1 1 * ?") // 1st day of every month
    public void autoGenerateReports() {
        int month = LocalDate.now().minusMonths(1).getMonthValue();
        int year = LocalDate.now().minusMonths(1).getYear();

        List<AttendanceSummaryDTO> summary = attendanceService.getMonthlyAttendanceSummary(month, year);
        List<AttendanceRankDTO> rank = attendanceService.getMonthlyAttendanceRank(month, year);

        System.out.println("✅ Monthly reports generated for " + month + "/" + year);
    }
}
