package com.clg.recommender.CollegeRecommender.controllers;

import com.clg.recommender.CollegeRecommender.model.Branch;
import com.clg.recommender.CollegeRecommender.model.Colloge;
import com.clg.recommender.CollegeRecommender.services.AdminCollegeService;
import com.clg.recommender.CollegeRecommender.services.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@RequestMapping("/admin/branches")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBranchController {

    @Autowired
    private BranchService branchService;

    @Autowired
    private AdminCollegeService collegeService;

    @GetMapping
    public String showAllBranches(Model model) {
        model.addAttribute("branches", branchService.getAllBranches());
        model.addAttribute("colleges", collegeService.getAllColleges());
        model.addAttribute("streams", Arrays.asList(Branch.Stream.values()));

        if (!model.containsAttribute("branch")) {
            model.addAttribute("branch", new Branch());
        }

        return "admin/branch";
    }

    @PostMapping("/save")
    public String saveBranch(@ModelAttribute("branch") Branch branch,
                             @RequestParam("collegeId") int collegeId,
                             RedirectAttributes redirectAttributes) {
        try {
            Colloge college = collegeService.getCollegeById((long) collegeId);
            if (college == null) {
                throw new RuntimeException("Selected College not found.");
            }
            branch.setCollege(college);

            if (branch.getBranchId() == 0) {
                branchService.saveBranch(branch);
                redirectAttributes.addFlashAttribute("successMessage", "Branch added successfully!");
            } else {
                branchService.updateBranch(branch.getBranchId(), branch);
                redirectAttributes.addFlashAttribute("successMessage", "Branch updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving branch: " + e.getMessage());
        }
        return "redirect:/admin/branches";
    }

    @GetMapping("/delete/{id}")
    public String deleteBranch(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            branchService.deleteBranch(id);
            redirectAttributes.addFlashAttribute("successMessage", "Branch deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting branch: " + e.getMessage());
        }
        return "redirect:/admin/branches";
    }
}
