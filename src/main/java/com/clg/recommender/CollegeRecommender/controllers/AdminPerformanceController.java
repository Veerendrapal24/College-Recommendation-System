package com.clg.recommender.CollegeRecommender.controllers;

import com.clg.recommender.CollegeRecommender.model.Colloge;
import com.clg.recommender.CollegeRecommender.model.PerformanceMetric;
import com.clg.recommender.CollegeRecommender.services.AdminCollegeService;
import com.clg.recommender.CollegeRecommender.services.PerformanceMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/performance")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPerformanceController {

    @Autowired
    private PerformanceMetricService metricService;

    @Autowired
    private AdminCollegeService collegeService;

    @GetMapping
    public String showAllMetrics(Model model) {
        List<PerformanceMetric> metrics = metricService.getAllMetrics();
        List<Colloge> colleges = collegeService.getAllColleges();

        Set<Integer> collegesWithMetrics = metrics.stream()
                .map(m -> m.getCollege().getCollegeId())
                .collect(Collectors.toSet());

        List<Colloge> collegesWithoutMetrics = colleges.stream()
                .filter(c -> !collegesWithMetrics.contains(c.getCollegeId()))
                .collect(Collectors.toList());

        model.addAttribute("metrics", metrics);
        model.addAttribute("colleges", colleges);
        model.addAttribute("collegesWithoutMetrics", collegesWithoutMetrics);

        if (!model.containsAttribute("metric")) {
            model.addAttribute("metric", new PerformanceMetric());
        }

        return "admin/performance";
    }

    @PostMapping("/save")
    public String saveMetric(@ModelAttribute("metric") PerformanceMetric metric,
                             @RequestParam("collegeId") int collegeId,
                             RedirectAttributes redirectAttributes) {
        try {
            Colloge college = collegeService.getCollegeById((long) collegeId);
            if (college == null) {
                throw new RuntimeException("Selected College not found.");
            }
            metric.setCollege(college);

            Optional<PerformanceMetric> existing = metricService.getMetricByCollege(college);

            if (metric.getPerformanceId() == 0) {
                if (existing.isPresent()) {
                    PerformanceMetric existingMetric = existing.get();
                    existingMetric.setPassPercentage(metric.getPassPercentage());
                    existingMetric.setNeetQualPercent(metric.getNeetQualPercent());
                    existingMetric.setMainsQualPercent(metric.getMainsQualPercent());
                    existingMetric.setSscCutoff(metric.getSscCutoff());
                    metricService.saveMetric(existingMetric);
                    redirectAttributes.addFlashAttribute("successMessage", "Performance metrics updated successfully");
                } else {
                    metricService.saveMetric(metric);
                    redirectAttributes.addFlashAttribute("successMessage", "Performance metrics created successfully");
                }
            } else {
                if (existing.isPresent() && existing.get().getPerformanceId() != metric.getPerformanceId()) {
                    PerformanceMetric existingMetric = existing.get();
                    existingMetric.setPassPercentage(metric.getPassPercentage());
                    existingMetric.setNeetQualPercent(metric.getNeetQualPercent());
                    existingMetric.setMainsQualPercent(metric.getMainsQualPercent());
                    existingMetric.setSscCutoff(metric.getSscCutoff());
                    metricService.saveMetric(existingMetric);
                    redirectAttributes.addFlashAttribute("successMessage", "Performance metrics updated successfully");
                } else {
                    metricService.updateMetric(metric.getPerformanceId(), metric);
                    redirectAttributes.addFlashAttribute("successMessage", "Performance metrics updated successfully");
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving performance metric: " + e.getMessage());
        }
        return "redirect:/admin/performance";
    }

    @GetMapping("/delete/{id}")
    public String deleteMetric(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            metricService.deleteMetric(id);
            redirectAttributes.addFlashAttribute("successMessage", "Performance metric deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting performance metric: " + e.getMessage());
        }
        return "redirect:/admin/performance";
    }
}
