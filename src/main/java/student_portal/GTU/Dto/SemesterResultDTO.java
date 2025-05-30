package student_portal.GTU.Dto;

public class SemesterResultDTO {
    private Long studentId;
//    private String studentName;
    private Integer semesterNumber;
    private String subjectCode;
    private String subjectName;
    private Integer marksObtained;
    private Integer maxMarks;


    public SemesterResultDTO(Long studentId,Integer semesterNumber, String subjectCode, String subjectName, Integer marksObtained, Integer maxMarks) {
        this.studentId = studentId;
//        this.studentName=studentName;
        this.semesterNumber = semesterNumber;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.marksObtained = marksObtained;
        this.maxMarks = maxMarks;
    }

//    public String getStudentName() {
//        return studentName;
//    }
//
//    public void setStudentName(String studentName) {
//        this.studentName = studentName;
//    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Integer getSemesterNumber() {
        return semesterNumber;
    }

    public void setSemesterNumber(Integer semesterNumber) {
        this.semesterNumber = semesterNumber;
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

    public Integer getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(Integer marksObtained) {
        this.marksObtained = marksObtained;
    }

    public Integer getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(Integer maxMarks) {
        this.maxMarks = maxMarks;
    }
}
