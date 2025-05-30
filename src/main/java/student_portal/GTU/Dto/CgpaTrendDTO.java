package student_portal.GTU.Dto;

public class CgpaTrendDTO {
    private Integer semester;
    private Double cgpa;

    public CgpaTrendDTO(Integer semester, Double cgpa) {
        this.semester = semester;
        this.cgpa = cgpa;
    }

    public Integer getSemester() {
        return semester;
    }

    public Double getCgpa() {
        return cgpa;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }
}
