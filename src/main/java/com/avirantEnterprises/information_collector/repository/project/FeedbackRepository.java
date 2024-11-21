package com.avirantEnterprises.information_collector.repository.project;

import com.avirantEnterprises.information_collector.model.project.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
}
