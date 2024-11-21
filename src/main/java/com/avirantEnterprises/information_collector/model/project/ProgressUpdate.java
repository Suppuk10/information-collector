package com.avirantEnterprises.information_collector.model.project;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "progress_update")
public class ProgressUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "milestone_update", nullable = false)
    private String milestoneUpdate;

    @Column(name = "progress_file_path")
    private String progressFilePath;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMilestoneUpdate() {
        return milestoneUpdate;
    }

    public void setMilestoneUpdate(String milestoneUpdate) {
        this.milestoneUpdate = milestoneUpdate;
    }

    public String getProgressFilePath() {
        return progressFilePath;
    }

    public void setProgressFilePath(String progressFilePath) {
        this.progressFilePath = progressFilePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
