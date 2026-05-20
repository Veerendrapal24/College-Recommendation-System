package com.clg.recommender.CollegeRecommender.model;


import jakarta.persistence.*;

@Entity
@Table(name = "branches")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int branchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id", nullable = false)
    private Colloge college;

    @Enumerated(EnumType.STRING)
    private Stream streamName;

    public enum Stream {
        MPC, BiPC, CEC, MEC
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public Stream getStreamName() {
        return streamName;
    }

    public void setStreamName(Stream streamName) {
        this.streamName = streamName;
    }

    public Colloge getCollege() {
        return college;
    }

    public void setCollege(Colloge college) {
        this.college = college;
    }

    public Branch() {
    }

    public Branch(int branchId, Stream streamName, Colloge college) {
        this.branchId = branchId;
        this.streamName = streamName;
        this.college = college;
    }
}
