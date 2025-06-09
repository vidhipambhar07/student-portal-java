package student_portal.GTU.ServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import student_portal.GTU.Dto.AttendanceRequestDTO;
import student_portal.GTU.Model.Attendance;
import student_portal.GTU.Model.Subject;
import student_portal.GTU.Model.User;
import student_portal.GTU.Repository.AttendanceRepository;
import student_portal.GTU.Repository.SubjectRepository;
import student_portal.GTU.Repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceImptTest {
    @InjectMocks
    private AttendanceServiceImpt attendanceService;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Test
    void testMarkAttendance_Success() {
        // Arrange
        Long studentId = 1L;
        Long subjectId = 10L;
        LocalDate lectureDate = LocalDate.now();

        AttendanceRequestDTO dto = new AttendanceRequestDTO();
        dto.setStudentId(studentId);
        dto.setSubjectId(subjectId);
        dto.setLectureDate(lectureDate);
        dto.setPresent(true);

        User mockStudent = new User();
        mockStudent.setId(studentId);

        Subject mockSubject = new Subject();
        mockSubject.setSubjectId(subjectId);

        when(attendanceRepository.existsByStudentIdAndLectureDateAndSubject_SubjectId(
                studentId, lectureDate, subjectId)).thenReturn(false);

        when(userRepository.findById(studentId)).thenReturn(Optional.of(mockStudent));
        when(subjectRepository.findById(subjectId)).thenReturn(Optional.of(mockSubject));

        Attendance savedAttendance = new Attendance();
        savedAttendance.setStudent(mockStudent);
        savedAttendance.setSubject(mockSubject);
        savedAttendance.setLectureDate(lectureDate);
        savedAttendance.setIsPresent(true);

        when(attendanceRepository.save(any(Attendance.class))).thenReturn(savedAttendance);

        // Act
        Attendance result = attendanceService.markAttendance(dto);

        // Assert
        assertNotNull(result);
        assertEquals(studentId, result.getStudent().getId());
        assertEquals(subjectId, result.getSubject().getSubjectId());
        assertEquals(lectureDate, result.getLectureDate());
        assertTrue(result.getIsPresent());

        verify(attendanceRepository).existsByStudentIdAndLectureDateAndSubject_SubjectId(studentId, lectureDate, subjectId);
        verify(userRepository).findById(studentId);
        verify(subjectRepository).findById(subjectId);
        verify(attendanceRepository).save(any(Attendance.class));
    }

    @Test
    void testMarkAttendance_AlreadyMarked_ThrowsException() {
        // Arrange
        AttendanceRequestDTO dto = new AttendanceRequestDTO();
        dto.setStudentId(1L);
        dto.setSubjectId(2L);
        dto.setLectureDate(LocalDate.now());

        when(attendanceRepository.existsByStudentIdAndLectureDateAndSubject_SubjectId(
                dto.getStudentId(), dto.getLectureDate(), dto.getSubjectId())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> attendanceService.markAttendance(dto));
        assertEquals("Attendance already marked for this student and subject on this date", exception.getMessage());

        verify(attendanceRepository).existsByStudentIdAndLectureDateAndSubject_SubjectId(dto.getStudentId(), dto.getLectureDate(), dto.getSubjectId());
        verify(userRepository, never()).findById(any());
        verify(subjectRepository, never()).findById(any());
        verify(attendanceRepository, never()).save(any());
    }
}