package com.clg.recommender.CollegeRecommender.services;

import com.clg.recommender.CollegeRecommender.config.RecommendationConstants;
import com.clg.recommender.CollegeRecommender.dto.CollegeDto;
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

    public List<CollegeDto> getFilteredPrivateColleges(String location, Double budget,
                                                       Boolean hostelAvailable, String branch) {
        System.out.println("Filter private colleges called. Budget limit: " + budget + ", Location: " + location + ", Branch: " + branch + ", Hostel: " + hostelAvailable);

        // 1. First attempt: Search using exact criteria
        List<Colloge> exactMatches = collegeRepository.findAll().stream()
                .filter(c -> c.getSector() == Colloge.Sector.Private)
                .filter(c -> location == null || location.trim().isEmpty() ||
                        c.getLocation().trim().equalsIgnoreCase(location.trim()))
                .filter(c -> budget == null || c.getBudgetRange() == null || (c.getBudgetRange() <= budget))
                .filter(c -> hostelAvailable == null || !hostelAvailable || c.isHostelAvailable() == hostelAvailable)
                .filter(c -> filterByBranch(c, branch))
                .collect(Collectors.toList());

        boolean isFallbackUsed = false;
        List<Colloge> finalColleges;
        
        if (!exactMatches.isEmpty()) {
            finalColleges = exactMatches;
        } else {
            // 2. Fallback: Search with budget increased by 15% (factor of 1.15)
            isFallbackUsed = true;
            Double expandedBudget = budget != null ? budget * RecommendationConstants.BUDGET_FALLBACK_FACTOR : null;
            System.out.println("No exact matches found. Triggering fallback budget: " + expandedBudget);
            
            finalColleges = collegeRepository.findAll().stream()
                    .filter(c -> c.getSector() == Colloge.Sector.Private)
                    .filter(c -> location == null || location.trim().isEmpty() ||
                            c.getLocation().trim().equalsIgnoreCase(location.trim()))
                    .filter(c -> expandedBudget == null || c.getBudgetRange() == null || (c.getBudgetRange() <= expandedBudget))
                    .filter(c -> hostelAvailable == null || !hostelAvailable || c.isHostelAvailable() == hostelAvailable)
                    .filter(c -> filterByBranch(c, branch))
                    .collect(Collectors.toList());
        }

        // 3. Map to DTOs and calculate weighted scoring
        final boolean fallbackFlag = isFallbackUsed;
        List<CollegeDto> dtoList = finalColleges.stream()
                .map(c -> {
                    double score = calculateScore(c, branch, budget, null, hostelAvailable, true);
                    List<String> reasons = generateMatchReasons(c, branch, budget, null, hostelAvailable, true, fallbackFlag);
                    return new CollegeDto(c, score, !fallbackFlag, fallbackFlag, reasons);
                })
                .sorted((d1, d2) -> {
                    // Sort: exact matches first, then score descending, then pass percentage descending
                    int exactCompare = Boolean.compare(d2.isExactMatch(), d1.isExactMatch());
                    if (exactCompare != 0) return exactCompare;
                    
                    int scoreCompare = Double.compare(d2.getScore(), d1.getScore());
                    if (scoreCompare != 0) return scoreCompare;
                    
                    double d1Pass = (d1.getPerformanceMetric() != null) ? d1.getPerformanceMetric().getPassPercentage() : 0.0;
                    double d2Pass = (d2.getPerformanceMetric() != null) ? d2.getPerformanceMetric().getPassPercentage() : 0.0;
                    return Double.compare(d2Pass, d1Pass);
                })
                .collect(Collectors.toList());

        System.out.println("Private recommendations generated: " + dtoList.size() + " matches.");
        return dtoList;
    }

    public List<CollegeDto> getFilteredGovernmentColleges(String location, Boolean hostelAvailable,
                                                          Double sscPercentage, String branch) {
        System.out.println("Filter government colleges called. GPA limit: " + sscPercentage + ", Location: " + location + ", Branch: " + branch + ", Hostel: " + hostelAvailable);

        // 1. First attempt: Search using exact criteria
        List<Colloge> exactMatches = collegeRepository.findAll().stream()
                .filter(c -> c.getSector() == Colloge.Sector.Government)
                .filter(c -> location == null || location.trim().isEmpty() ||
                        c.getLocation().trim().equalsIgnoreCase(location.trim()))
                .filter(c -> sscPercentage == null ||
                        (c.getPerformanceMetric() != null &&
                                c.getPerformanceMetric().getSscCutoff() != null &&
                                sscPercentage >= c.getPerformanceMetric().getSscCutoff()))
                .filter(c -> hostelAvailable == null || !hostelAvailable || c.isHostelAvailable() == hostelAvailable)
                .filter(c -> filterByBranch(c, branch))
                .collect(Collectors.toList());

        boolean isFallbackUsed = false;
        List<Colloge> finalColleges;

        if (!exactMatches.isEmpty()) {
            finalColleges = exactMatches;
        } else {
            // 2. Fallback: Search with GPA cutoff expanded: GPA + 0.5 capped at 10.0
            isFallbackUsed = true;
            Double expandedSsc = sscPercentage != null ? Math.min(sscPercentage + RecommendationConstants.GPA_FALLBACK_INCREMENT, 10.0) : null;
            System.out.println("No exact matches found. Triggering fallback GPA limit: " + expandedSsc);

            finalColleges = collegeRepository.findAll().stream()
                    .filter(c -> c.getSector() == Colloge.Sector.Government)
                    .filter(c -> location == null || location.trim().isEmpty() ||
                            c.getLocation().trim().equalsIgnoreCase(location.trim()))
                    .filter(c -> expandedSsc == null ||
                            (c.getPerformanceMetric() != null &&
                                    c.getPerformanceMetric().getSscCutoff() != null &&
                                    expandedSsc >= c.getPerformanceMetric().getSscCutoff()))
                    .filter(c -> hostelAvailable == null || !hostelAvailable || c.isHostelAvailable() == hostelAvailable)
                    .filter(c -> filterByBranch(c, branch))
                    .collect(Collectors.toList());
        }

        // 3. Map to DTOs and calculate weighted scoring
        final boolean fallbackFlag = isFallbackUsed;
        List<CollegeDto> dtoList = finalColleges.stream()
                .map(c -> {
                    double score = calculateScore(c, branch, null, sscPercentage, hostelAvailable, false);
                    List<String> reasons = generateMatchReasons(c, branch, null, sscPercentage, hostelAvailable, false, fallbackFlag);
                    return new CollegeDto(c, score, !fallbackFlag, fallbackFlag, reasons);
                })
                .sorted((d1, d2) -> {
                    // Sort: exact matches first, then score descending, then pass percentage descending
                    int exactCompare = Boolean.compare(d2.isExactMatch(), d1.isExactMatch());
                    if (exactCompare != 0) return exactCompare;
                    
                    int scoreCompare = Double.compare(d2.getScore(), d1.getScore());
                    if (scoreCompare != 0) return scoreCompare;
                    
                    double d1Pass = (d1.getPerformanceMetric() != null) ? d1.getPerformanceMetric().getPassPercentage() : 0.0;
                    double d2Pass = (d2.getPerformanceMetric() != null) ? d2.getPerformanceMetric().getPassPercentage() : 0.0;
                    return Double.compare(d2Pass, d1Pass);
                })
                .collect(Collectors.toList());

        System.out.println("Government recommendations generated: " + dtoList.size() + " matches.");
        return dtoList;
    }

    public double calculateScore(Colloge c, String stream, Double budget, Double sscPercentage, Boolean hostelAvailable, boolean isPrivate) {
        double score = 0.0;

        // 1. Stream Match (40% - 40 points)
        boolean streamMatched = false;
        if (stream == null || stream.trim().isEmpty() || stream.equalsIgnoreCase("all")) {
            streamMatched = true;
        } else {
            String normalizedStream = stream.trim().toUpperCase();
            if (c.getBranches() != null) {
                streamMatched = c.getBranches().stream()
                        .anyMatch(b -> b.getStreamName() != null && b.getStreamName().name().toUpperCase().equals(normalizedStream));
            }
        }
        if (streamMatched) {
            score += RecommendationConstants.SCORE_STREAM * 100;
        }

        // 2. Budget Fit (30% - 30 points)
        if (isPrivate) {
            if (budget == null || budget <= 0) {
                score += RecommendationConstants.SCORE_BUDGET * 100; // Default full score if budget is not provided
            } else if (c.getBudgetRange() != null) {
                double collegeFee = c.getBudgetRange();
                if (collegeFee <= budget) {
                    score += RecommendationConstants.SCORE_BUDGET * 100; // Cheaper or equal, full score
                } else {
                    // Decay scoring based on distance
                    double budgetFit = Math.max(0.0, 1.0 - ((collegeFee - budget) / budget));
                    score += RecommendationConstants.SCORE_BUDGET * (budgetFit * 100);
                }
            }
        } else {
            // Government sector has low or negligible fees, give full budget score to keep rankings fair
            score += RecommendationConstants.SCORE_BUDGET * 100;
        }

        // 3. Performance Metrics Fit (20% - 20 points)
        double perfFit = 0.0;
        if (c.getPerformanceMetric() != null) {
            if (isPrivate) {
                // Private: combine pass rate, jee mains, and neet percentages
                double pass = c.getPerformanceMetric().getPassPercentage();
                double neet = c.getPerformanceMetric().getNeetQualPercent();
                double jee = c.getPerformanceMetric().getMainsQualPercent();
                perfFit = (pass + neet + jee) / 300.0; // range 0 to 1
            } else {
                // Government: ssc cutoff compared with student GPA
                Double cutoff = c.getPerformanceMetric().getSscCutoff();
                if (cutoff != null && sscPercentage != null && sscPercentage > 0) {
                    if (cutoff <= sscPercentage) {
                        perfFit = cutoff / sscPercentage; // closer to their cutoff is a better match
                    } else {
                        perfFit = Math.max(0.0, 1.0 - ((cutoff - sscPercentage) / sscPercentage));
                    }
                }
            }
        }
        score += RecommendationConstants.SCORE_PERFORMANCE * (perfFit * 100);

        // 4. Hostel Availability Fit (10% - 10 points)
        boolean hostelMatch = (hostelAvailable == null || !hostelAvailable || c.isHostelAvailable());
        if (hostelMatch) {
            score += RecommendationConstants.SCORE_HOSTEL * 100;
        }

        return score;
    }

    private List<String> generateMatchReasons(Colloge c, String stream, Double budget, Double sscPercentage, Boolean hostelAvailable, boolean isPrivate, boolean isFallback) {
        List<String> reasons = new ArrayList<>();

        // Stream Match Reason
        if (stream != null && !stream.trim().isEmpty() && !stream.equalsIgnoreCase("all")) {
            reasons.add("Stream matched (" + stream.trim().toUpperCase() + ")");
        } else {
            reasons.add("Stream matched");
        }

        // Budget Reason
        if (isPrivate) {
            if (c.getBudgetRange() != null) {
                if (isFallback) {
                    reasons.add("Nearby budget option (₹" + String.format("%,.0f", c.getBudgetRange()) + ")");
                } else {
                    reasons.add("Within budget (₹" + String.format("%,.0f", c.getBudgetRange()) + ")");
                }
            }
        } else {
            reasons.add("Low cost government education");
        }

        // Hostel Reason
        if (c.isHostelAvailable()) {
            reasons.add("Hostel available");
        }

        // Performance Reason
        if (c.getPerformanceMetric() != null) {
            double passRate = c.getPerformanceMetric().getPassPercentage();
            if (passRate >= 85.0) {
                reasons.add("Strong academic performance (" + String.format("%.1f", passRate) + "% Pass Rate)");
            } else {
                reasons.add("Stable academic record (" + String.format("%.1f", passRate) + "% Pass Rate)");
            }
        }

        return reasons;
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

    public List<Branch.Stream> getAvailableStreams() {
        return Arrays.asList(Branch.Stream.values());
    }

    public List<String> getUniqueLocations() {
        return collegeRepository.findDistinctLocations();
    }

    public static class LocationResult {
        private List<String> locations;
        private boolean fallback;
        private String message;

        public LocationResult(List<String> locations, boolean fallback, String message) {
            this.locations = locations;
            this.fallback = fallback;
            this.message = message;
        }

        public List<String> getLocations() { return locations; }
        public void setLocations(List<String> locations) { this.locations = locations; }
        public boolean isFallback() { return fallback; }
        public void setFallback(boolean fallback) { this.fallback = fallback; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public LocationResult getFilteredLocations(String sector, String streamName, Double budget, Double sscPercentage) {
        try {
            Branch.Stream stream = Branch.Stream.valueOf(streamName.trim().toUpperCase());
            if ("Private".equalsIgnoreCase(sector)) {
                if (budget == null) budget = 250000.0;
                List<String> locations = collegeRepository.findPrivateLocationsByStreamAndBudget(stream, budget);
                if (locations == null || locations.isEmpty()) {
                    // Fallback: expand budget by configured budget fallback constant factor
                    double expandedBudget = budget * RecommendationConstants.BUDGET_FALLBACK_FACTOR;
                    List<String> fallbackLocations = collegeRepository.findPrivateLocationsByStreamAndBudget(stream, expandedBudget);
                    if (fallbackLocations != null && !fallbackLocations.isEmpty()) {
                        return new LocationResult(fallbackLocations, true, "No exact matches found. Showing nearby budget options.");
                    }
                    return new LocationResult(Collections.emptyList(), false, "");
                }
                return new LocationResult(locations, false, "");
            } else if ("Government".equalsIgnoreCase(sector)) {
                if (sscPercentage == null) sscPercentage = 10.0;
                List<String> locations = collegeRepository.findGovernmentLocationsByStreamAndSscCutoff(stream, sscPercentage);
                if (locations == null || locations.isEmpty()) {
                    // Fallback: expand cutoff GPA by adding 0.5 capped at 10.0
                    double expandedSsc = Math.min(10.0, sscPercentage + RecommendationConstants.GPA_FALLBACK_INCREMENT);
                    List<String> fallbackLocations = collegeRepository.findGovernmentLocationsByStreamAndSscCutoff(stream, expandedSsc);
                    if (fallbackLocations != null && !fallbackLocations.isEmpty()) {
                        return new LocationResult(fallbackLocations, true, "No exact matches found. Showing colleges with nearby GPA cutoffs.");
                    }
                    return new LocationResult(Collections.emptyList(), false, "");
                }
                return new LocationResult(locations, false, "");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid stream: " + streamName);
        }
        return new LocationResult(Collections.emptyList(), false, "");
    }
}