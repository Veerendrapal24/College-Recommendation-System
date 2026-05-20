package com.clg.recommender.CollegeRecommender.services;


import com.clg.recommender.CollegeRecommender.model.Colloge;
import java.util.List;

public interface AdminCollegeService {
    List<Colloge> getAllColleges();
    Colloge getCollegeById(Long id);
    void saveCollege(Colloge college);
    void updateCollege(Long id, Colloge updated);
    void deleteCollege(Long id);
}
