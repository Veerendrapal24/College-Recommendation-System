package com.clg.recommender.CollegeRecommender.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "colleges")
public class Colloge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int collegeId;

    private String name;
    private String location;

    @Enumerated(EnumType.STRING)
    private Sector sector;

    private boolean hostelAvailable;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private Double budgetRange;

    @OneToOne(mappedBy = "college", cascade = CascadeType.ALL)
    private PerformanceMetric performanceMetric;

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Branch> branches;

    public enum Sector {
        Government, Private
    }

    // Getters and Setters
    public int getCollegeId() {
        return collegeId;
    }
    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public Sector getSector() {
        return sector;
    }
    public void setSector(Sector sector) {
        this.sector = sector;
    }
    public boolean isHostelAvailable() {
        return hostelAvailable;
    }
    public void setHostelAvailable(boolean hostelAvailable) {
        this.hostelAvailable = hostelAvailable;
    }
    public Double getBudgetRange() {
        return budgetRange;
    }
    public void setBudgetRange(Double budgetRange) {
        this.budgetRange = budgetRange;
    }
    public PerformanceMetric getPerformanceMetric() {
        return performanceMetric;
    }
    public void setPerformanceMetric(PerformanceMetric performanceMetric) {
        this.performanceMetric = performanceMetric;
    }
    public List<Branch> getBranches() {
        return branches;
    }
    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

}