package student_portal.GTU.Dto;

public class CourseDifficultyDTO {
    private String subjectCode;
    private String subjectName;
    private Long totalStudents;
    private Long failedStudents;
    private double failureRate;

    public CourseDifficultyDTO() {

    }

    public CourseDifficultyDTO(String subjectCode, String subjectName, Long totalStudents, Long failedStudents, double failureRate) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.totalStudents = totalStudents;
        this.failedStudents = failedStudents;
        this.failureRate = failureRate;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(Long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public Long getFailedStudents() {
        return failedStudents;
    }

    public void setFailedStudents(Long failedStudents) {
        this.failedStudents = failedStudents;
    }

    public double getFailureRate() {
        return failureRate;
    }

    public void setFailureRate(double failureRate) {
        this.failureRate = failureRate;
    }
}
