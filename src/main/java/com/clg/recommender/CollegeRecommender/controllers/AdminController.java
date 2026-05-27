package com.clg.recommender.CollegeRecommender.controllers;

import com.clg.recommender.CollegeRecommender.model.User;
import com.clg.recommender.CollegeRecommender.services.AdminCollegeService;
import com.clg.recommender.CollegeRecommender.services.BranchService;
import com.clg.recommender.CollegeRecommender.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminCollegeService collegeService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Add dashboard statistics
        int totalColleges = collegeService.getAllColleges().size();
        int totalBranches = branchService.getAllBranches().size();
        int totalUsers = userService.getAllUsers().size();

        model.addAttribute("totalColleges", totalColleges);
        model.addAttribute("totalBranches", totalBranches);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("systemStatus", "Online");
        
        // Fetch lists
        java.util.List<com.clg.recommender.CollegeRecommender.model.Colloge> collegesList = collegeService.getAllColleges();
        java.util.List<com.clg.recommender.CollegeRecommender.model.Branch> branchesList = branchService.getAllBranches();
        java.util.List<User> usersList = userService.getAllUsers();

        // 1. Sector counts
        long privateCount = collegesList.stream().filter(c -> c.getSector() == com.clg.recommender.CollegeRecommender.model.Colloge.Sector.Private).count();
        long governmentCount = collegesList.stream().filter(c -> c.getSector() == com.clg.recommender.CollegeRecommender.model.Colloge.Sector.Government).count();
        model.addAttribute("privateCollegesCount", privateCount);
        model.addAttribute("governmentCollegesCount", governmentCount);

        // 2. Stream counts
        java.util.Map<String, Long> streamCounts = branchesList.stream()
                .filter(b -> b.getStreamName() != null)
                .collect(java.util.stream.Collectors.groupingBy(b -> b.getStreamName().name(), java.util.stream.Collectors.counting()));
        model.addAttribute("streamCounts", streamCounts);

        // 3. User role counts
        long adminCount = usersList.stream().filter(u -> u.getRole() == com.clg.recommender.CollegeRecommender.model.Role.ADMIN).count();
        long studentCount = usersList.stream().filter(u -> u.getRole() == com.clg.recommender.CollegeRecommender.model.Role.STUDENT).count();
        model.addAttribute("adminUsersCount", adminCount);
        model.addAttribute("studentUsersCount", studentCount);

        // 4. Performance averages (primitives can't be null)
        double avgPass = collegesList.stream()
                .filter(c -> c.getPerformanceMetric() != null)
                .mapToDouble(c -> c.getPerformanceMetric().getPassPercentage())
                .average().orElse(0.0);
        double avgNeet = collegesList.stream()
                .filter(c -> c.getPerformanceMetric() != null)
                .mapToDouble(c -> c.getPerformanceMetric().getNeetQualPercent())
                .average().orElse(0.0);
        double avgJee = collegesList.stream()
                .filter(c -> c.getPerformanceMetric() != null)
                .mapToDouble(c -> c.getPerformanceMetric().getMainsQualPercent())
                .average().orElse(0.0);
        model.addAttribute("avgPassPercentage", avgPass);
        model.addAttribute("avgNeetQualPercent", avgNeet);
        model.addAttribute("avgMainsQualPercent", avgJee);

        // Format a current last login time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy, hh:mm a");
        model.addAttribute("lastLogin", now.format(formatter));

        // Get logged in admin info
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String email = authentication.getName();
            User admin = userService.getAllUsers().stream()
                    .filter(u -> u.getEmail().equals(email))
                    .findFirst()
                    .orElse(null);
            if (admin != null) {
                model.addAttribute("adminName", admin.getName());
                model.addAttribute("adminEmail", admin.getEmail());
            } else {
                model.addAttribute("adminName", "Administrator");
                model.addAttribute("adminEmail", email);
            }
        } else {
            model.addAttribute("adminName", "Administrator");
            model.addAttribute("adminEmail", "admin@college.edu");
        }

        return "admin/dashboard";
    }
}