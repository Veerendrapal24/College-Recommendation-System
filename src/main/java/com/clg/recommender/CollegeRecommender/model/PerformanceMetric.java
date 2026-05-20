package com.clg.recommender.CollegeRecommender.model;

import jakarta.persistence.*;

@Entity
@Table(name = "performance_metrics")
public class PerformanceMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int performanceId;

    @OneToOne
    @JoinColumn(name = "college_id")
    private Colloge college;

    @Column(columnDefinition = "DECIMAL(5,2)")  // For percentages like 92.50%
    private double passPercentage;

    @Column(columnDefinition = "DECIMAL(5,2)")
    private double neetQualPercent;

    @Column(columnDefinition = "DECIMAL(5,2)")

    private double mainsQualPercent;

    @Column(columnDefinition = "DECIMAL(4,2)")  // For SSC cutoff (e.g., 8.50)
    private Double sscCutoff; // Nullable

    // Constructors, getters and setters remain the same
    public PerformanceMetric() {
    }

    public PerformanceMetric(int performanceId, Double sscCutoff, double mainsQualPercent,
                             double neetQualPercent, double passPercentage, Colloge college) {
        this.performanceId = performanceId;
        this.sscCutoff = sscCutoff;
        this.mainsQualPercent = mainsQualPercent;
        this.neetQualPercent = neetQualPercent;
        this.passPercentage = passPercentage;
        this.college = college;
    }

    // All getters and setters remain unchanged
    public int getPerformanceId() { return performanceId; }
    public void setPerformanceId(int performanceId) { this.performanceId = performanceId; }
    public Colloge getCollege() { return college; }
    public void setCollege(Colloge college) { this.college = college; }
    public double getPassPercentage() { return passPercentage; }
    public void setPassPercentage(double passPercentage) { this.passPercentage = passPercentage; }
    public double getNeetQualPercent() { return neetQualPercent; }
    public void setNeetQualPercent(double neetQualPercent) { this.neetQualPercent = neetQualPercent; }
    public double getMainsQualPercent() { return mainsQualPercent; }
    public void setMainsQualPercent(double mainsQualPercent) { this.mainsQualPercent = mainsQualPercent; }
    public Double getSscCutoff() { return sscCutoff; }
    public void setSscCutoff(Double sscCutoff) { this.sscCutoff = sscCutoff; }
}