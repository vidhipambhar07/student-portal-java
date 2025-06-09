package student_portal.GTU.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import student_portal.GTU.Dto.AttendanceRankDTO;
import student_portal.GTU.Dto.AttendanceRequestDTO;
import student_portal.GTU.Dto.AttendanceSummaryDTO;
import student_portal.GTU.Model.Attendance;
import student_portal.GTU.Model.User;
import student_portal.GTU.Repository.AttendanceRepository;
import student_portal.GTU.Repository.SubjectRepository;
import student_portal.GTU.Repository.UserRepository;
import student_portal.GTU.Service.AttendanceService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceServiceImpt implements AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public Attendance markAttendance(AttendanceRequestDTO dto) {
        if (attendanceRepository.existsByStudentIdAndLectureDateAndSubject_SubjectId(
                dto.getStudentId(), dto.getLectureDate(), dto.getSubjectId())) {
            throw new RuntimeException("Attendance already marked for this student and subject on this date");
        }
        Attendance attendance = new Attendance();
        attendance.setStudent(userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found")));
        attendance.setSubject(subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found")));
        attendance.setLectureDate(dto.getLectureDate());
        attendance.setIsPresent(dto.isPresent());

        return attendanceRepository.save(attendance);
    }

    public List<AttendanceSummaryDTO> getMonthlyAttendanceSummary(int month, int year) {
        List<Object[]> rawData = attendanceRepository.getMonthlyAttendanceSummary(month, year);
        List<AttendanceSummaryDTO> summaryList = new ArrayList<>();

        for (Object[] row : rawData) {
            Long studentId = ((Number) row[0]).longValue();
            int attended = ((Number) row[1]).intValue();

            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            summaryList.add(new AttendanceSummaryDTO(studentId, student.getUsername(), attended));
        }

        return summaryList;
    }

    public List<AttendanceRankDTO> getMonthlyAttendanceRank(int month, int year) {
        List<Object[]> rawData = attendanceRepository.getMonthlyAttendancePoints(month, year);
        List<AttendanceRankDTO> rankList = new ArrayList<>();
        int rank = 1;

        for (Object[] row : rawData) {
            Long studentId = ((Number) row[0]).longValue();
            int points = ((Number) row[1]).intValue();

            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            rankList.add(new AttendanceRankDTO(studentId, student.getUsername(), points, rank++));
        }

        return rankList;
    }


}
