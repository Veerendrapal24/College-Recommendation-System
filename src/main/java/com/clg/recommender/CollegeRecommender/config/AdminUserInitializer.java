package com.clg.recommender.CollegeRecommender.config;

import com.clg.recommender.CollegeRecommender.model.Role;
import com.clg.recommender.CollegeRecommender.model.User;
import com.clg.recommender.CollegeRecommender.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createAdminUsersIfNotExist();
    }

    private void createAdminUsersIfNotExist() {
        // Check if admin user already exists
        if (!userRepository.existsByEmail("admin@college.com")) {
            System.out.println("Creating admin user: admin@college.com");

            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail("admin@college.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
            System.out.println("✅ Admin user created successfully!");
        } else {
            System.out.println("Admin user already exists.");
        }

        // Check if manager user already exists
        if (!userRepository.existsByEmail("manager@college.com")) {
            System.out.println("Creating manager user: manager@college.com");

            User manager = new User();
            manager.setName("Content Manager");
            manager.setEmail("manager@college.com");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setRole(Role.ADMIN);

            userRepository.save(manager);
            System.out.println("✅ Manager user created successfully!");
        } else {
            System.out.println("Manager user already exists.");
        }

        // Check if super admin user already exists
        if (!userRepository.existsByEmail("superadmin@college.com")) {
            System.out.println("Creating super admin user: superadmin@college.com");

            User superAdmin = new User();
            superAdmin.setName("Super Admin");
            superAdmin.setEmail("superadmin@college.com");
            superAdmin.setPassword(passwordEncoder.encode("super123"));
            superAdmin.setRole(Role.ADMIN);

            userRepository.save(superAdmin);
            System.out.println("✅ Super Admin user created successfully!");
        } else {
            System.out.println("Super Admin user already exists.");
        }

        // Update existing users to have STUDENT role if they don't have a role
        updateExistingUsersRole();
    }

    private void updateExistingUsersRole() {
        System.out.println("Checking for users without roles...");

        // This assumes your User entity has a method to find users with null roles
        // If your UserRepository doesn't have this method, you can add it
        try {
            userRepository.findAll().forEach(user -> {
                if (user.getRole() == null) {
                    user.setRole(Role.STUDENT);
                    userRepository.save(user);
                    System.out.println("Updated user " + user.getEmail() + " to STUDENT role");
                }
            });
        } catch (Exception e) {
            System.out.println("Note: Some users may need manual role assignment");
        }
    }
}