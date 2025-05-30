package student_portal.GTU.Dto;

public class MonthlyPerformanceDTO {
    private Long studentId;
    private String studentName;
    private int attendancePoints;
    private Integer rank;
    public MonthlyPerformanceDTO(Long studentId, String studentName, int attendancePoints,Integer rank) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.attendancePoints = attendancePoints;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
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

    public int getAttendancePoints() {
        return attendancePoints;
    }

    public void setAttendancePoints(int attendancePoints) {
        this.attendancePoints = attendancePoints;
    }
}
