package com.clg.recommender.CollegeRecommender.repositories;

import com.clg.recommender.CollegeRecommender.model.Branch;
import com.clg.recommender.CollegeRecommender.model.Colloge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CollegeRepository extends JpaRepository<Colloge, Integer> {
    @Query("SELECT DISTINCT c.location FROM Colloge c WHERE c.location IS NOT NULL AND c.location != '' ORDER BY c.location ASC")
    List<String> findDistinctLocations();

    @Query("SELECT DISTINCT c.location FROM Colloge c JOIN c.branches b WHERE c.sector = com.clg.recommender.CollegeRecommender.model.Colloge.Sector.Private AND b.streamName = :streamName AND c.budgetRange <= :budget AND c.location IS NOT NULL AND c.location != '' ORDER BY c.location ASC")
    List<String> findPrivateLocationsByStreamAndBudget(
            @Param("streamName") com.clg.recommender.CollegeRecommender.model.Branch.Stream streamName,
            @Param("budget") Double budget
    );

    @Query("SELECT DISTINCT c.location FROM Colloge c JOIN c.branches b LEFT JOIN c.performanceMetric p WHERE c.sector = com.clg.recommender.CollegeRecommender.model.Colloge.Sector.Government AND b.streamName = :streamName AND (p.sscCutoff IS NULL OR p.sscCutoff <= :sscPercentage) AND c.location IS NOT NULL AND c.location != '' ORDER BY c.location ASC")
    List<String> findGovernmentLocationsByStreamAndSscCutoff(
            @Param("streamName") com.clg.recommender.CollegeRecommender.model.Branch.Stream streamName,
            @Param("sscPercentage") Double sscPercentage
    );
}
