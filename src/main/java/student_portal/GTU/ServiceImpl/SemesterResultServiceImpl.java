package student_portal.GTU.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import student_portal.GTU.Dto.*;
import student_portal.GTU.Model.SemesterResult;
import student_portal.GTU.Repository.SemesterResultRepository;
import student_portal.GTU.Service.SemesterResultService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SemesterResultServiceImpl implements SemesterResultService {

    @Autowired
    SemesterResultRepository semesterResultRepository;



    @Override
    public List<SemesterGroupedResultDTO> getResultsGroupedBySemester(Long studentId) {
        // 1. Fetch flat list of semester results for the student
        List<SemesterResultDTO> flatResults = semesterResultRepository.findAllSemesterResultsByStudentId(studentId);

        // 2. Group by semester number and map to SemesterGroupedResultDTO
        Map<Integer, List<SemesterSubjectDTO>> grouped = flatResults.stream()
                .collect(Collectors.groupingBy(
                        SemesterResultDTO::getSemesterNumber,
                        Collectors.mapping(result -> new SemesterSubjectDTO(
                                result.getSubjectCode(),
                                result.getSubjectName(),
                                result.getMarksObtained(),
                                result.getMaxMarks()
                        ), Collectors.toList())
                ));

        // 3. Convert map entries to sorted list of DTOs
        return grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new SemesterGroupedResultDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SemesterRankingDTO> calculateMeritRanking(Long studentId) {
        List<SemesterResultDTO> allResults = semesterResultRepository.findAllSemesterResultsByStudentId(studentId); // Include student_id, marks, max_marks, semester
//        Map<Long, String> studentNames = new HashMap<>();

        Map<Integer, List<SemesterResultDTO>> resultsBySemester = allResults.stream()
                .collect(Collectors.groupingBy(SemesterResultDTO::getSemesterNumber));

        List<SemesterRankingDTO> response = new ArrayList<>();

        for (Map.Entry<Integer, List<SemesterResultDTO>> entry : resultsBySemester.entrySet()) {
            int semester = entry.getKey();
            List<SemesterResultDTO> semesterResults = entry.getValue();

            // Group by studentId
            Map<Long, Integer> studentPoints = new HashMap<>();

            for (SemesterResultDTO result : semesterResults) {
                double percentage = (result.getMarksObtained() * 100.0) / result.getMaxMarks();
                int points;

                if (percentage >= 90) points = 5;
                else if (percentage >= 75) points = 4;
                else if (percentage >= 35) points = 3;
                else points = 0;

                studentPoints.merge(result.getStudentId(), points, Integer::sum);
            }

            // Sort by points
            List<Map.Entry<Long, Integer>> sorted = studentPoints.entrySet()
                    .stream()
                    .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                    .collect(Collectors.toList());

            // Find rank
            int rank = 1;
            for (Map.Entry<Long, Integer> entryRank : sorted) {
                if (entryRank.getKey().equals(studentId)) {
                    SemesterRankingDTO dto = new SemesterRankingDTO();
                    dto.setSemester(semester);
                    dto.setRank(rank);
                    dto.setPoints(entryRank.getValue());
                    dto.setTopperId(sorted.get(0).getKey());
                    response.add(dto);
                    break;
                }
                rank++;
            }
        }

        return response;
    }
    @Override
    public List<CgpaTrendDTO> getCgpaTrend(Long studentId) {
        List<SemesterResult> results = semesterResultRepository.findByStudentId(studentId);

        // Group by semester
        Map<Integer, List<SemesterResult>> groupedBySemester = results.stream()
                .collect(Collectors.groupingBy(sr -> sr.getSemester().getSemesterNumber()));

        List<CgpaTrendDTO> cgpaTrend = new ArrayList<>();

        for (Map.Entry<Integer, List<SemesterResult>> entry : groupedBySemester.entrySet()) {
            int semester = entry.getKey();
            List<SemesterResult> semesterResults = entry.getValue();

            double totalMarks = semesterResults.stream().mapToDouble(SemesterResult::getMarksObtained).sum();
            double maxMarks = semesterResults.stream().mapToDouble(SemesterResult::getMaxMarks).sum();

            double percentage = (totalMarks / maxMarks) * 100;

            double cgpa = percentageToCGPA(percentage);
            cgpaTrend.add(new CgpaTrendDTO(semester, cgpa));
        }

        // Sort by semester
        cgpaTrend.sort(Comparator.comparingInt(CgpaTrendDTO::getSemester));
        return cgpaTrend;
    }

    public double percentageToCGPA(double percentage) {
        return percentage / 10.0;
    }
    @Override
    public List<CourseDifficultyDTO> getCourseDifficultyAnalytics() {
        return semesterResultRepository.getCourseDifficultyAnalytics();

    }


}
