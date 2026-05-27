package com.clg.recommender.CollegeRecommender.services;

import com.clg.recommender.CollegeRecommender.model.Colloge;
import com.clg.recommender.CollegeRecommender.model.PerformanceMetric;
import com.clg.recommender.CollegeRecommender.repositories.PerformanceMetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerformanceMetricServiceImp implements PerformanceMetricService {

    @Autowired
    private PerformanceMetricRepository repository;

    @Override
    public List<PerformanceMetric> getAllMetrics() {
        return repository.findAll();
    }

    @Override
    public PerformanceMetric getMetricById(Integer id) {
        Optional<PerformanceMetric> metric = repository.findById(id);
        return metric.orElse(null);
    }

    @Override
    public Optional<PerformanceMetric> getMetricByCollege(Colloge college) {
        return repository.findByCollege(college);
    }

    @Override
    public void saveMetric(PerformanceMetric metric) {
        repository.save(metric);
    }

    @Override
    public void updateMetric(Integer id, PerformanceMetric updated) {
        Optional<PerformanceMetric> existingOpt = repository.findById(id);
        if (existingOpt.isPresent()) {
            PerformanceMetric existing = existingOpt.get();
            existing.setCollege(updated.getCollege());
            existing.setPassPercentage(updated.getPassPercentage());
            existing.setNeetQualPercent(updated.getNeetQualPercent());
            existing.setMainsQualPercent(updated.getMainsQualPercent());
            existing.setSscCutoff(updated.getSscCutoff());
            repository.save(existing);
        } else {
            throw new RuntimeException("PerformanceMetric with ID " + id + " not found");
        }
    }

    @Override
    public void deleteMetric(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("PerformanceMetric with ID " + id + " not found");
        }
    }
}
