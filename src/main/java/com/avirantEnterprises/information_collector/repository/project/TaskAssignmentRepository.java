package com.avirantEnterprises.information_collector.repository.project;

import com.avirantEnterprises.information_collector.model.project.TaskAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment,Long> {
}
