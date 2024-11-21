package com.avirantEnterprises.information_collector.controller.project;

import com.avirantEnterprises.information_collector.service.project.ProgressUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ProgressUpdateController {

    @Autowired
    private ProgressUpdateService service;

    // GET method to render the progressUpdate.html page
    @GetMapping("/progressUpdate")
    public String showProgressUpdateForm(
            @RequestParam(value = "success", required = false) String success,
            @RequestParam(value = "error", required = false) String error,
            Model model
    ) {
        if (success != null) {
            model.addAttribute("message", "Progress update submitted successfully!");
            model.addAttribute("messageType", "success");
        } else if (error != null) {
            model.addAttribute("message", "An error occurred while submitting the progress update.");
            model.addAttribute("messageType", "error");
        }
        return "/project/progressUpdate"; // Renders progressUpdate.html
    }

    // POST method to handle form submission
    @PostMapping("/progressUpdate")
    public String submitProgressUpdate(
            @RequestParam("project-name") String projectName,
            @RequestParam("milestone-update") String milestoneUpdate,
            @RequestParam(value = "progress-file", required = false) MultipartFile progressFile
    ) {
        try {
            service.saveProgressUpdate(projectName, milestoneUpdate, progressFile);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/progressUpdate?error=true";
        }
        return "redirect:/progressUpdate?success=true";
    }
}
