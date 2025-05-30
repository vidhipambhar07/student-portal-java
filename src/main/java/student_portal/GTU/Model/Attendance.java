package student_portal.GTU.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "attendance", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "lecture_date", "subject_id"})
})
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many attendance records belong to one student
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(name = "lecture_date", nullable = false)
    private LocalDate lectureDate;

    // Many attendance records belong to one subject
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;


    @Column(name = "is_present", nullable = false)
    private Boolean isPresent;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructor(s)
    public Attendance() {
        this.createdAt = LocalDateTime.now();
    }

    public Attendance(User student, LocalDate lectureDate, Subject subject, Boolean isPresent) {
        this.student = student;
        this.lectureDate = lectureDate;
        this.subject = subject;
        this.isPresent = isPresent;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public LocalDate getLectureDate() {
        return lectureDate;
    }

    public void setLectureDate(LocalDate lectureDate) {
        this.lectureDate = lectureDate;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Boolean getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(Boolean isPresent) {
        this.isPresent = isPresent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
