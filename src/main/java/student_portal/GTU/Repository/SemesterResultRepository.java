package student_portal.GTU.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import student_portal.GTU.Dto.CourseDifficultyDTO;
import student_portal.GTU.Dto.SemesterResultDTO;
import student_portal.GTU.Model.SemesterResult;

import java.util.List;

public interface SemesterResultRepository extends JpaRepository<SemesterResult, Long> {

    @Query("SELECT new student_portal.GTU.Dto.SemesterResultDTO(" +
            "sr.student.id,sr.semester.semesterNumber, sr.subject.subjectCode, sr.subject.subjectName, " +
            "sr.marksObtained, sr.maxMarks) " +
            "FROM SemesterResult sr WHERE sr.student.id = :studentId " +
            "ORDER BY sr.semester.semesterNumber, sr.subject.subjectCode")
    List<SemesterResultDTO> findAllSemesterResultsByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT sr FROM SemesterResult sr WHERE sr.student.id = :studentId")
    List<SemesterResult> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT new student_portal.GTU.Dto.CourseDifficultyDTO(" +
            "sr.subject.subjectCode, sr.subject.subjectName, " +
            "COUNT(sr), SUM(CASE WHEN sr.marksObtained < 35 THEN 1 ELSE 0 END), " +
            "100.0 * SUM(CASE WHEN sr.marksObtained < 35 THEN 1 ELSE 0 END) / COUNT(sr)) " +
            "FROM SemesterResult sr " +
            "GROUP BY sr.subject.subjectCode, sr.subject.subjectName " +
            "HAVING SUM(CASE WHEN sr.marksObtained < 35 THEN 1 ELSE 0 END) > 0")
    List<CourseDifficultyDTO> getCourseDifficultyAnalytics();


}