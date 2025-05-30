package student_portal.GTU.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SemesterRankingDTO {
    private int semester;
    private int rank;
    private int points;
    private Long topperId;
    private String topperName;

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Long getTopperId() {
        return topperId;
    }

    public void setTopperId(Long topperId) {
        this.topperId = topperId;
    }

    public String getTopperName() {
        return topperName;
    }

    public void setTopperName(String topperName) {
        this.topperName = topperName;
    }
}
