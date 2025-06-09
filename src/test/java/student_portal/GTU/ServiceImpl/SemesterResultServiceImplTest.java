package student_portal.GTU.ServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import student_portal.GTU.Dto.*;
import student_portal.GTU.Model.Semester;
import student_portal.GTU.Model.SemesterResult;
import student_portal.GTU.Model.Subject;
import student_portal.GTU.Model.User;
import student_portal.GTU.Repository.SemesterResultRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class SemesterResultServiceImplTest {
    @InjectMocks
    private SemesterResultServiceImpl semesterResultService;

    @Mock
    private SemesterResultRepository semesterResultRepository;

    @Test
    void testGetResultsGroupedBySemester() {
        Long studentId = 1L;

        // Mock flat results
        List<SemesterResultDTO> mockResults = List.of(
                new SemesterResultDTO(1L, 1, "234", "Data Structures", 100, 200),
                new SemesterResultDTO(1L, 1, "CS102", "Algorithms", 100, 200),
                new SemesterResultDTO(1L, 2, "CS201", "DBMS", 100, 100)
        );


        when(semesterResultRepository.findAllSemesterResultsByStudentId(studentId))
                .thenReturn(mockResults);

        // Call the service
        List<SemesterGroupedResultDTO> result = semesterResultService.getResultsGroupedBySemester(studentId);

        assertEquals(2, result.size()); // Two semesters

        // Check semester 1
        SemesterGroupedResultDTO semester1 = result.get(0);
        assertEquals(1, semester1.getSemester());
        assertEquals(2, semester1.getSubjects().size());

        // Check semester 2
        SemesterGroupedResultDTO semester2 = result.get(1);
        assertEquals(2, semester2.getSemester());
        assertEquals(1, semester2.getSubjects().size());

        // Verify mapping
        SemesterSubjectDTO firstSubject = semester1.getSubjects().get(0);
        assertEquals("234", firstSubject.getSubjectCode());
        assertEquals("Data Structures", firstSubject.getSubjectName());
        assertEquals(100, firstSubject.getMarksObtained());
        assertEquals(200, firstSubject.getMaxMarks());

        // Verify repository called once
        verify(semesterResultRepository).findAllSemesterResultsByStudentId(studentId);
    }

    @Test
    void testCalculateMeritRanking() {
        Long currentStudentId = 1L;
        Long otherStudentId = 2L;

        List<SemesterResultDTO> mockResults = List.of(
                new SemesterResultDTO(currentStudentId, 1, "DS", "Data Structures", 90, 100),
                new SemesterResultDTO(currentStudentId, 1, "Algo", "Algorithms", 95, 100),
                new SemesterResultDTO(otherStudentId, 1, "DS", "Data Structures", 85, 100),
                new SemesterResultDTO(otherStudentId, 1, "Algo", "Algorithms", 75, 100),

                new SemesterResultDTO(currentStudentId, 2, "DBMS", "DBMS", 80, 100),
                new SemesterResultDTO(otherStudentId, 2, "DBMS", "DBMS", 95, 100)
        );

        when(semesterResultRepository.findAllSemesterResultsByStudentId(currentStudentId))
                .thenReturn(mockResults);

        List<SemesterRankingDTO> rankings = semesterResultService.calculateMeritRanking(currentStudentId);

        assertEquals(2, rankings.size());

        SemesterRankingDTO semester1 = rankings.get(0);
        assertEquals(1, semester1.getSemester());
        assertEquals(1, semester1.getRank()); // currentStudent should be 1st
        assertEquals(10, semester1.getPoints()); // 5+5
        assertEquals(currentStudentId, semester1.getTopperId());

        SemesterRankingDTO semester2 = rankings.get(1);
        assertEquals(2, semester2.getSemester());
        assertEquals(2, semester2.getRank()); // currentStudent should be 2nd
        assertEquals(4, semester2.getPoints()); // 4 points
        assertEquals(otherStudentId, semester2.getTopperId());

        verify(semesterResultRepository).findAllSemesterResultsByStudentId(currentStudentId);
    }
    @Test
    void testGetCgpaTrend() {
        Long studentId = 1L;

        Semester semester1 = new Semester();
        semester1.setSemesterNumber(1);

        Semester semester2 = new Semester();
        semester2.setSemesterNumber(2);

        // Create mock Subject objects
        Subject subject1 = new Subject();
        subject1.setSubjectName("DS");

        Subject subject2 = new Subject();
        subject2.setSubjectName("Algo");

        Subject subject3 = new Subject();
        subject3.setSubjectName("DBMS");

        Subject subject4 = new Subject();
        subject4.setSubjectName("CN");

        User student = new User();
        student.setId(studentId);

        List<SemesterResult> mockResults = List.of(
                new SemesterResult(1L, semester1, subject1, student, 90, 100),
                new SemesterResult(2L, semester1, subject2, student, 80, 100),
                new SemesterResult(3L, semester2, subject3, student, 70, 100),
                new SemesterResult(4L, semester2, subject4, student, 60, 100)
        );

        when(semesterResultRepository.findByStudentId(studentId)).thenReturn(mockResults);

        List<CgpaTrendDTO> trend = semesterResultService.getCgpaTrend(studentId);

        assertEquals(2, trend.size());

        CgpaTrendDTO semester1Dto = trend.get(0);
        assertEquals(1, semester1Dto.getSemester());
        assertEquals(semesterResultService.percentageToCGPA(85.0), semester1Dto.getCgpa());

        CgpaTrendDTO semester2Dto = trend.get(1);
        assertEquals(2, semester2Dto.getSemester());
        assertEquals(semesterResultService.percentageToCGPA(65.0), semester2Dto.getCgpa());

        verify(semesterResultRepository).findByStudentId(studentId);
    }
}