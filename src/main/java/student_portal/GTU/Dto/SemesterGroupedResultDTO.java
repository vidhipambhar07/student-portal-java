package student_portal.GTU.Dto;

import java.util.List;

public class SemesterGroupedResultDTO {
    private Integer semester;
    private List<SemesterSubjectDTO> subjects;

    public SemesterGroupedResultDTO() {
    }

    public SemesterGroupedResultDTO(Integer semester, List<SemesterSubjectDTO> subjects) {
        this.semester = semester;
        this.subjects = subjects;
    }

    public Integer getSemester() {
        return semester;
    }

    public List<SemesterSubjectDTO> getSubjects() {
        return subjects;
    }
}
