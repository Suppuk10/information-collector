package com.avirantEnterprises.information_collector.controller.project;

import com.avirantEnterprises.information_collector.service.project.ResourceAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class ResourceAllocationController {

    @Autowired
    private ResourceAllocationService service;

    // GET method to render the resource allocation page
    @GetMapping("/resourceAllocation")
    public String showResourceAllocationForm() {
        return "/project/resourceAllocation";
    }

    // POST method to handle the form submission
    @PostMapping("/resourceAllocation")
    public String submitResourceAllocation(
            @RequestParam("resource-name") String resourceName,
            @RequestParam("quantity") int quantity,
            @RequestParam("allocated-by") String allocatedBy,
            @RequestParam("allocated-to") String allocatedTo,
            @RequestParam("allocation-date") String allocationDate,
            @RequestParam("time-duration") String timeDuration,
            Model model
    ) {
        try {
            // Save the resource allocation details to the database
            service.saveResourceAllocation(
                    resourceName,
                    quantity,
                    allocatedBy,
                    allocatedTo,
                    LocalDate.parse(allocationDate),
                    timeDuration
            );
            // Adding success message to the model
            model.addAttribute("successMessage", "Resource allocation saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            // Adding error message to the model
            model.addAttribute("errorMessage", "Failed to save resource allocation.");
        }
        return "/project/resourceAllocation";  // Return the view name
    }
}
