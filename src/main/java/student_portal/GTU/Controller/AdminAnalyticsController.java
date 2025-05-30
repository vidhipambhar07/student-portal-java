package student_portal.GTU.Controller;


import student_portal.GTU.Dto.CourseDifficultyDTO;
import student_portal.GTU.Service.SemesterResultService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminAnalyticsController {

    @Autowired
    private SemesterResultService semesterResultService;

    @GetMapping("/analytics/course-difficulty")
    public List<CourseDifficultyDTO> getCourseDifficultyAnalytics() {
        return semesterResultService.getCourseDifficultyAnalytics();
    }
}
