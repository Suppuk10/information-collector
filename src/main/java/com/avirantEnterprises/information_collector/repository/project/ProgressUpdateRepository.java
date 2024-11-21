package com.avirantEnterprises.information_collector.repository.project;

import com.avirantEnterprises.information_collector.model.project.ProgressUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressUpdateRepository extends JpaRepository<ProgressUpdate, Long> {
}
