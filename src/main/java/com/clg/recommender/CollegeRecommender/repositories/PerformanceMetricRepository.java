package com.clg.recommender.CollegeRecommender.repositories;

import com.clg.recommender.CollegeRecommender.model.Colloge;
import com.clg.recommender.CollegeRecommender.model.PerformanceMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PerformanceMetricRepository extends JpaRepository<PerformanceMetric, Integer> {
    Optional<PerformanceMetric> findByCollege(Colloge college);
}

