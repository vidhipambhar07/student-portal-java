package student_portal.GTU.Dto;

public class AttendanceRankDTO {
    private Long studentId;
    private String studentName;
    private int points;
    private int rank;

    public AttendanceRankDTO(Long studentId, String studentName, int points, int rank) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.points = points;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
