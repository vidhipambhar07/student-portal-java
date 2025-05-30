package student_portal.GTU.Dto;

public class SemesterMeritDTO {
    private Long semesterId;
    private Long studentId;
    private Integer totalPoints;
    private Integer studentRank;
    private String rating;
    private Double cgpa;  // or BigDecimal for precision

    public SemesterMeritDTO(Object semesterId, Object studentId, Object totalPoints, Object studentRank) {
        this.semesterId = ((Number) semesterId).longValue();
        this.studentId = ((Number) studentId).longValue();
        this.totalPoints = ((Number) totalPoints).intValue();
        this.studentRank = ((Number) studentRank).intValue();
    }

    public Double getCgpa() {
        return cgpa;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Integer getStudentRank() {
        return studentRank;
    }

    public void setStudentRank(Integer studentRank) {
        this.studentRank = studentRank;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }


}
