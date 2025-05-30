package student_portal.GTU.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "monthly_report", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "report_month"})
})
public class MonthlyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(name = "report_month", nullable = false)
    private LocalDate reportMonth; // store first day of month

    @Column(name = "attendance_score", nullable = false)
    private Integer attendanceScore;

    @Column(name = "performance_score", nullable = false)
    private Integer performanceScore;

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructor, getters, setters, etc.
}
