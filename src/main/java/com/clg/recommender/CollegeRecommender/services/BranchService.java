package com.clg.recommender.CollegeRecommender.services;

import com.clg.recommender.CollegeRecommender.model.Branch;
import java.util.List;

public interface BranchService {
    List<Branch> getAllBranches();
    Branch getBranchById(Integer id);
    void saveBranch(Branch branch);
    void updateBranch(Integer id, Branch branch);
    void deleteBranch(Integer id);
}
