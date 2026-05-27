package com.clg.recommender.CollegeRecommender.controllers;

import com.clg.recommender.CollegeRecommender.model.Role;
import com.clg.recommender.CollegeRecommender.model.User;
import com.clg.recommender.CollegeRecommender.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", Arrays.asList(Role.values()));

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }

        return "admin/users";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user,
                           RedirectAttributes redirectAttributes) {
        try {
            if (user.getId() == null || user.getId() == 0) {
                // New user
                userService.saveUser(user);
                redirectAttributes.addFlashAttribute("successMessage", "User added successfully!");
            } else {
                // Existing user
                userService.updateUser(user.getId(), user);
                redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                String loggedInEmail = auth.getName();
                User loggedInUser = userService.getAllUsers().stream()
                        .filter(u -> u.getEmail().equalsIgnoreCase(loggedInEmail))
                        .findFirst()
                        .orElse(null);
                if (loggedInUser != null && loggedInUser.getId().equals(id)) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Error: You cannot delete your own admin account!");
                    return "redirect:/admin/users";
                }
            }
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
