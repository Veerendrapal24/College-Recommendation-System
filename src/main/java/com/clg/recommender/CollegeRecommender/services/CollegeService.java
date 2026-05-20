package com.clg.recommender.CollegeRecommender.services;

import com.clg.recommender.CollegeRecommender.model.Branch;
import com.clg.recommender.CollegeRecommender.model.Colloge;
import com.clg.recommender.CollegeRecommender.repositories.BranchRepository;
import com.clg.recommender.CollegeRecommender.repositories.CollegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CollegeService {
    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private BranchRepository branchRepository;

    public List<Colloge> getFilteredPrivateColleges(String location, Double budget,
                                                    Boolean hostelAvailable, String branch) {
        return collegeRepository.findAll().stream()
                .filter(c -> c.getSector() == Colloge.Sector.Private)
                .filter(c -> location == null || location.trim().isEmpty() ||
                        c.getLocation().toLowerCase().contains(location.trim().toLowerCase()))
                .filter(c -> budget == null || c.getBudgetRange() == null ||
                        (budget >= c.getBudgetRange()))
                .filter(c -> hostelAvailable == null ||
                        c.isHostelAvailable() == hostelAvailable)
                .filter(c -> filterByBranch(c, branch))
                .sorted(compareByPassPercentage())
                .collect(Collectors.toList());
    }

    public List<Colloge> getFilteredGovernmentColleges(String location, Boolean hostelAvailable,
                                                       Double sscPercentage, String branch) {
        return collegeRepository.findAll().stream()
                .filter(c -> c.getSector() == Colloge.Sector.Government)
                .filter(c -> location == null || location.trim().isEmpty() ||
                        c.getLocation().toLowerCase().contains(location.trim().toLowerCase()))
                .filter(c -> hostelAvailable == null ||
                        c.isHostelAvailable() == hostelAvailable)
                .filter(c -> sscPercentage == null ||
                        (c.getPerformanceMetric() != null &&
                                c.getPerformanceMetric().getSscCutoff() != null &&
                                sscPercentage >= c.getPerformanceMetric().getSscCutoff()))
                .filter(c -> filterByBranch(c, branch))
                .sorted(compareByPassPercentage())
                .collect(Collectors.toList());
    }

    private boolean filterByBranch(Colloge college, String branch) {
        if (branch == null || branch.trim().isEmpty() || branch.equalsIgnoreCase("all")) {
            return true;
        }

        List<Branch> branches = branchRepository.findByCollege(college);
        if (branches == null || branches.isEmpty()) {
            return false;
        }

        String normalizedBranch = branch.trim().toUpperCase();
        return branches.stream()
                .filter(Objects::nonNull)
                .map(Branch::getStreamName)
                .filter(Objects::nonNull)
                .anyMatch(stream -> stream.name().toUpperCase().equals(normalizedBranch));
    }

    private Comparator<Colloge> compareByPassPercentage() {
        return (c1, c2) -> {
            double c1Pass = (c1.getPerformanceMetric() != null) ? c1.getPerformanceMetric().getPassPercentage() : 0.0;
            double c2Pass = (c2.getPerformanceMetric() != null) ? c2.getPerformanceMetric().getPassPercentage() : 0.0;
            return Double.compare(c2Pass, c1Pass); // Descending order
        };
    }

    public List<Branch.Stream> getAvailableStreams() {
        return Arrays.asList(Branch.Stream.values());
    }
}