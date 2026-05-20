package com.clg.recommender.CollegeRecommender.services;

import com.clg.recommender.CollegeRecommender.model.Colloge;
import com.clg.recommender.CollegeRecommender.repositories.CollegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminCollegeServiceImp implements AdminCollegeService {

    @Autowired
    private CollegeRepository collegeRepository;

    @Override
    public List<Colloge> getAllColleges() {
        return collegeRepository.findAll();
    }

    @Override
    public Colloge getCollegeById(Long id) {
        Optional<Colloge> college = collegeRepository.findById(Math.toIntExact(id));
        return college.orElse(null);
    }

    @Override
    public void saveCollege(Colloge college) {
        collegeRepository.save(college);
    }

    @Override
    public void updateCollege(Long id, Colloge updated) {
        Optional<Colloge> existingOpt = collegeRepository.findById(Math.toIntExact(id));

        if (existingOpt.isPresent()) {
            Colloge existing = existingOpt.get();

            // Update all fields
            existing.setName(updated.getName());
            existing.setLocation(updated.getLocation());
            existing.setSector(updated.getSector());
            existing.setHostelAvailable(updated.isHostelAvailable());
            existing.setBudgetRange(updated.getBudgetRange());

            // Only update performance metric if it's provided
            if (updated.getPerformanceMetric() != null) {
                existing.setPerformanceMetric(updated.getPerformanceMetric());
            }

            // Only update branches if they're provided
            if (updated.getBranches() != null) {
                existing.setBranches(updated.getBranches());
            }

            collegeRepository.save(existing);
        } else {
            throw new RuntimeException("College with ID " + id + " not found");
        }
    }

    @Override
    public void deleteCollege(Long id) {
        if (collegeRepository.existsById(Math.toIntExact(id))) {
            collegeRepository.deleteById(Math.toIntExact(id));
        } else {
            throw new RuntimeException("College with ID " + id + " not found");
        }
    }
}