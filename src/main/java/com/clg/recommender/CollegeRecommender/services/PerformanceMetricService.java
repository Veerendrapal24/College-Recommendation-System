package com.clg.recommender.CollegeRecommender.services;

import com.clg.recommender.CollegeRecommender.model.Colloge;
import com.clg.recommender.CollegeRecommender.model.PerformanceMetric;
import java.util.List;
import java.util.Optional;

public interface PerformanceMetricService {
    List<PerformanceMetric> getAllMetrics();
    PerformanceMetric getMetricById(Integer id);
    Optional<PerformanceMetric> getMetricByCollege(Colloge college);
    void saveMetric(PerformanceMetric metric);
    void updateMetric(Integer id, PerformanceMetric metric);
    void deleteMetric(Integer id);
}
