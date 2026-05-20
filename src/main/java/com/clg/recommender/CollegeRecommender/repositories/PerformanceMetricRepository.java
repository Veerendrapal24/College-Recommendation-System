package com.clg.recommender.CollegeRecommender.repositories;

import com.clg.recommender.CollegeRecommender.model.PerformanceMetric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceMetricRepository extends JpaRepository<PerformanceMetric, Integer> {
}

