package com.avirantEnterprises.information_collector.service.project;

import com.avirantEnterprises.information_collector.model.project.ResourceAllocation;
import com.avirantEnterprises.information_collector.repository.project.ResourceAllocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ResourceAllocationService {

    @Autowired
    private ResourceAllocationRepository repository;

    public void saveResourceAllocation(
            String resourceName,
            int quantity,
            String allocatedBy,
            String allocatedTo,
            LocalDate allocationDate,
            String timeDuration
    ) {
        ResourceAllocation allocation = new ResourceAllocation();
        allocation.setResourceName(resourceName);
        allocation.setQuantity(quantity);
        allocation.setAllocatedBy(allocatedBy);
        allocation.setAllocatedTo(allocatedTo);
        allocation.setAllocationDate(allocationDate);
        allocation.setTimeDuration(timeDuration);

        repository.save(allocation);
    }
}
