package com.clg.recommender.CollegeRecommender.controllers;

import com.clg.recommender.CollegeRecommender.model.User;
import com.clg.recommender.CollegeRecommender.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "signup", required = false) String signup,
                            Model model) {

        if (error != null) model.addAttribute("error", "Invalid email or password");
        if (logout != null) model.addAttribute("success", "You've been logged out successfully");
        if (signup != null) model.addAttribute("success", "Registration successful! Please login");

        return "login";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute("user") User user,
                               BindingResult result,
                               Model model) {
        try {
            userService.registerUser(user);
            return "redirect:/login?signup=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

}