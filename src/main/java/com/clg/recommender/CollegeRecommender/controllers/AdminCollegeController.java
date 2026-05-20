package com.clg.recommender.CollegeRecommender.controllers;

import com.clg.recommender.CollegeRecommender.model.Colloge;
import com.clg.recommender.CollegeRecommender.services.AdminCollegeServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/colleges")
public class AdminCollegeController {

    @Autowired
    private AdminCollegeServiceImp collegeService;

    @GetMapping
    public String showAllColleges(Model model) {
        model.addAttribute("colleges", collegeService.getAllColleges());

        // Add a fresh college object for the add form
        if (!model.containsAttribute("college")) {
            model.addAttribute("college", new Colloge());
        }

        return "admin/college";
    }

    @PostMapping("/save")
    public String saveCollege(@ModelAttribute("college") Colloge college,
                              RedirectAttributes redirectAttributes) {
        try {
            if (college.getCollegeId() == 0) {
                // New college - add operation
                collegeService.saveCollege(college);
                redirectAttributes.addFlashAttribute("successMessage", "College added successfully!");
            } else {
                // Existing college - update operation
                collegeService.updateCollege((long) college.getCollegeId(), college);
                redirectAttributes.addFlashAttribute("successMessage", "College updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving college: " + e.getMessage());
        }
        return "redirect:/admin/colleges";
    }

    @GetMapping("/delete/{id}")
    public String deleteCollege(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            collegeService.deleteCollege((long) id);
            redirectAttributes.addFlashAttribute("successMessage", "College deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting college: " + e.getMessage());
        }
        return "redirect:/admin/colleges";
    }

    @GetMapping("/edit/{id}")
    public String editCollege(@PathVariable Integer id, Model model) {
        Colloge college = collegeService.getCollegeById((long)id);
        model.addAttribute("college", college);
        model.addAttribute("isEdit", true);
        return "admin/college";
    }
}