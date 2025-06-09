package student_portal.GTU.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class SemesterResult {

    public SemesterResult(Long resultId, Semester semester, Subject subject, User student, Integer marksObtained, Integer maxMarks) {
        this.resultId = resultId;
        this.semester = semester;
        this.subject = subject;
        this.student = student;
        this.marksObtained = marksObtained;
        this.maxMarks = maxMarks;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;  // or 'Student' entity

    @Column(name = "marks_obtained")
    private Integer marksObtained;

    @Column(name = "max_marks")
    private Integer maxMarks;

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
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

