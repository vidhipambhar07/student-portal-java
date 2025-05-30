package student_portal.GTU.Dto;

public class SemesterSubjectDTO {
    private String subjectCode;
    private String subjectName;
    private Integer marksObtained;
    private Integer maxMarks;

    public SemesterSubjectDTO(String subjectCode, String subjectName, Integer marksObtained, Integer maxMarks) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.marksObtained = marksObtained;
        this.maxMarks = maxMarks;
    }

    // Getters
    public String getSubjectCode() { return subjectCode; }
    public String getSubjectName() { return subjectName; }
    public Integer getMarksObtained() { return marksObtained; }
    public Integer getMaxMarks() { return maxMarks; }
}
