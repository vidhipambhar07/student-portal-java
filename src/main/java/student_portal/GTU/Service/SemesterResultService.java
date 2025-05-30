package student_portal.GTU.Service;

import org.springframework.stereotype.Service;
import student_portal.GTU.Dto.*;

import java.util.List;

@Service
public interface SemesterResultService {
    List<SemesterGroupedResultDTO> getResultsGroupedBySemester(Long studentId);
    List<SemesterRankingDTO> calculateMeritRanking(Long studentId);
    List<CgpaTrendDTO> getCgpaTrend(Long studentId);
    List<CourseDifficultyDTO> getCourseDifficultyAnalytics();

}
