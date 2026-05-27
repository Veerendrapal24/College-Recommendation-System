package com.clg.recommender.CollegeRecommender.services;

import com.clg.recommender.CollegeRecommender.model.Branch;
import com.clg.recommender.CollegeRecommender.repositories.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchServiceImp implements BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Override
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    @Override
    public Branch getBranchById(Integer id) {
        Optional<Branch> branch = branchRepository.findById(id);
        return branch.orElse(null);
    }

    @Override
    public void saveBranch(Branch branch) {
        branchRepository.save(branch);
    }

    @Override
    public void updateBranch(Integer id, Branch updated) {
        Optional<Branch> existingOpt = branchRepository.findById(id);
        if (existingOpt.isPresent()) {
            Branch existing = existingOpt.get();
            existing.setCollege(updated.getCollege());
            existing.setStreamName(updated.getStreamName());
            branchRepository.save(existing);
        } else {
            throw new RuntimeException("Branch with ID " + id + " not found");
        }
    }

    @Override
    public void deleteBranch(Integer id) {
        if (branchRepository.existsById(id)) {
            branchRepository.deleteById(id);
        } else {
            throw new RuntimeException("Branch with ID " + id + " not found");
        }
    }
}
