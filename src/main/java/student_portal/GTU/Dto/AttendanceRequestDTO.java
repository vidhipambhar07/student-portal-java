package student_portal.GTU.Dto;

import java.time.LocalDate;

public class AttendanceRequestDTO {
    private Long studentId;
    private Long subjectId;
    private LocalDate lectureDate;
    private boolean isPresent;

    public AttendanceRequestDTO(Long studentId, boolean isPresent, LocalDate lectureDate, Long subjectId) {
        this.studentId = studentId;
        this.isPresent = isPresent;
        this.lectureDate = lectureDate;
        this.subjectId = subjectId;
    }

    public AttendanceRequestDTO() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public LocalDate getLectureDate() {
        return lectureDate;
    }

    public void setLectureDate(LocalDate lectureDate) {
        this.lectureDate = lectureDate;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
}
