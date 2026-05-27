package com.clg.recommender.CollegeRecommender.dto;

import com.clg.recommender.CollegeRecommender.model.Colloge;
import java.util.List;

public class CollegeDto {
    private Colloge college;
    private double score;
    private boolean exactMatch;
    private boolean fallbackMatch;
    private List<String> matchReasons;

    public CollegeDto(Colloge college, double score, boolean exactMatch, boolean fallbackMatch, List<String> matchReasons) {
        this.college = college;
        this.score = score;
        this.exactMatch = exactMatch;
        this.fallbackMatch = fallbackMatch;
        this.matchReasons = matchReasons;
    }

    // Delegate getters to match Colloge's signature for easy rendering in existing templates
    public int getCollegeId() {
        return college.getCollegeId();
    }

    public String getName() {
        return college.getName();
    }

    public String getLocation() {
        return college.getLocation();
    }

    public Colloge.Sector getSector() {
        return college.getSector();
    }

    public boolean isHostelAvailable() {
        return college.isHostelAvailable();
    }

    public Double getBudgetRange() {
        return college.getBudgetRange();
    }

    public com.clg.recommender.CollegeRecommender.model.PerformanceMetric getPerformanceMetric() {
        return college.getPerformanceMetric();
    }

    public List<com.clg.recommender.CollegeRecommender.model.Branch> getBranches() {
        return college.getBranches();
    }

    public Colloge getCollege() {
        return college;
    }

    public double getScore() {
        return score;
    }

    public boolean isExactMatch() {
        return exactMatch;
    }

    public boolean isFallbackMatch() {
        return fallbackMatch;
    }

    public List<String> getMatchReasons() {
        return matchReasons;
    }
}
