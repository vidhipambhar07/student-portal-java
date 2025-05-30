package student_portal.GTU.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import student_portal.GTU.Model.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
 boolean existsByStudentIdAndLectureDateAndSubject_SubjectId(Long studentId, LocalDate lectureDate, Long subjectId);
// @Query(value = "SELECT student_id, COUNT(*) FROM attendance " +
//         "WHERE is_present = 0 AND MONTH(lecture_date) = :month AND YEAR(lecture_date) = :year " +
//         "GROUP BY student_id", nativeQuery = true)
// List<Object[]> getMonthlyAttendancePoints(@Param("month") int month, @Param("year") int year);
//
// @Query(value = "SELECT student_id, COUNT(*) as points " +
//         "FROM attendance " +
//         "WHERE is_present = 1 AND MONTH(lecture_date) = :month AND YEAR(lecture_date) = :year " +
//         "GROUP BY student_id " +
//         "ORDER BY points DESC", nativeQuery = true)
// List<Object[]> getStudentAttendancePoints(@Param("month") int month, @Param("year") int year);


 @Query(value = "SELECT student_id, COUNT(*) as attended " +
         "FROM attendance " +
         "WHERE is_present = 0 AND MONTH(lecture_date) = :month AND YEAR(lecture_date) = :year " +
         "GROUP BY student_id", nativeQuery = true)
 List<Object[]> getMonthlyAttendanceSummary(@Param("month") int month, @Param("year") int year);

 @Query(value = "SELECT student_id, COUNT(*) as points " +
         "FROM attendance " +
         "WHERE is_present = 0 AND MONTH(lecture_date) = :month AND YEAR(lecture_date) = :year " +
         "GROUP BY student_id " +
         "ORDER BY points DESC", nativeQuery = true)
 List<Object[]> getMonthlyAttendancePoints(@Param("month") int month, @Param("year") int year);


}
