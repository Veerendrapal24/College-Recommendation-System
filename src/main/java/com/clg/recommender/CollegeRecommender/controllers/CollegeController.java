package com.clg.recommender.CollegeRecommender.controllers;

import com.clg.recommender.CollegeRecommender.model.Branch;
import com.clg.recommender.CollegeRecommender.model.Colloge;
import com.clg.recommender.CollegeRecommender.services.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CollegeController {
    @Autowired
    private CollegeService collegeService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/colleges/private/filter")
    public String filterPrivateColleges(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double budget,
            @RequestParam(required = false) Boolean hostelAvailable,
            @RequestParam(required = false) String branch,
            Model model) {

        List<Colloge> colleges = collegeService.getFilteredPrivateColleges(
                location, budget, hostelAvailable, branch
        );

        List<Branch.Stream> availableStreams = collegeService.getAvailableStreams();

        model.addAttribute("colleges", colleges);
        model.addAttribute("location", location);
        model.addAttribute("budget", budget);
        model.addAttribute("hostelAvailable", hostelAvailable);
        model.addAttribute("branch", branch);
        model.addAttribute("availableStreams", availableStreams);

        return "privateColleges";
    }

    @GetMapping("/governmentColleges")
    public String getGovernmentColleges(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Boolean hostelAvailable,
            @RequestParam(required = false) Double sscPercentage,
            @RequestParam(required = false) String branch,
            Model model) {

        List<Colloge> colleges = collegeService.getFilteredGovernmentColleges(
                location, hostelAvailable, sscPercentage, branch
        );

        List<Branch.Stream> availableStreams = collegeService.getAvailableStreams();

        model.addAttribute("colleges", colleges);
        model.addAttribute("location", location);
        model.addAttribute("hostelAvailable", hostelAvailable);
        model.addAttribute("sscPercentage", sscPercentage);
        model.addAttribute("branch", branch);
        model.addAttribute("availableStreams", availableStreams);

        return "government";
    }
}