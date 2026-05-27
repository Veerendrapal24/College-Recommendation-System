package com.clg.recommender.CollegeRecommender.config;

public final class RecommendationConstants {
    private RecommendationConstants() {
        // Prevent instantiation
    }

    public static final double BUDGET_FALLBACK_FACTOR = 1.15;
    public static final double GPA_FALLBACK_INCREMENT = 0.5;
    
    public static final double SCORE_STREAM = 0.40;
    public static final double SCORE_BUDGET = 0.30;
    public static final double SCORE_PERFORMANCE = 0.20;
    public static final double SCORE_HOSTEL = 0.10;
}
