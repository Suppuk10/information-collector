package com.avirantEnterprises.information_collector.repository.project;

import com.avirantEnterprises.information_collector.model.project.ResourceAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceAllocationRepository extends JpaRepository<ResourceAllocation, Long> {
}