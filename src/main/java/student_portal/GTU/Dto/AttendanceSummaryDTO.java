package student_portal.GTU.Dto;

public class AttendanceSummaryDTO {
    private Long studentId;
    private String studentName;
    private int lecturesAttended;

    public AttendanceSummaryDTO(Long studentId, String studentName, int lecturesAttended) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.lecturesAttended = lecturesAttended;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getLecturesAttended() {
        return lecturesAttended;
    }

    public void setLecturesAttended(int lecturesAttended) {
        this.lecturesAttended = lecturesAttended;
    }
}
