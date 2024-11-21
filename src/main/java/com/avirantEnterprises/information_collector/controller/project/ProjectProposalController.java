package com.avirantEnterprises.information_collector.controller.project;

import com.avirantEnterprises.information_collector.model.project.ProjectProposal;
import com.avirantEnterprises.information_collector.repository.project.ProjectProposalRepository;
import com.avirantEnterprises.information_collector.service.project.ProjectProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
public class ProjectProposalController {

    @Autowired
    private ProjectProposalService projectProposalService;

    @Value("${file.pdf-dir}")
    private String pdfDir;

    @GetMapping("/projectProposalForm")
    public String projectProposalForm(Model model) {
        model.addAttribute("projectProposal", new ProjectProposal());
        return "project/proposalform/projectProposalForm";
    }

    @PostMapping("/submitProjectProposal")
    public String createProjectProposal(@RequestParam("projectTitle") String projectTitle,
                                        @RequestParam("projectDescription") String projectDescription,
                                        @RequestParam("department") String department,
                                        @RequestParam("startDate") String startDate,
                                        @RequestParam("estimatedBudget") Double estimatedBudget,
                                        @RequestParam("attachment") MultipartFile attachment,
                                        Model model) {
        try {
            projectProposalService.createProjectProposal(projectTitle, projectDescription, department,
                    startDate, estimatedBudget, attachment);
            model.addAttribute("message", "Project proposal submitted successfully!");
            return "project/proposalform/success";
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Could not upload attachment: " + e.getMessage());
            return "project/proposalform/projectProposalForm";
        }
    }

    @GetMapping("/viewProjectProposals")
    public String viewAllProjectProposals(Model model) {
        List<ProjectProposal> proposals = projectProposalService.getAllProjectProposals();

        // Clean attachment paths (optional: do this in the service itself)
        proposals.forEach(proposal -> {
            if (proposal.getAttachment() != null) {
                String fullPath = proposal.getAttachment();
                String filename = Paths.get(fullPath).getFileName().toString(); // Extract the filename
                proposal.setAttachment(filename); // Replace with the cleaned filename
            }
        });

        model.addAttribute("proposals", proposals);
        return "project/proposalform/DisplayProjectProposals";
    }

    @GetMapping("/viewProposalPdf/{filename:.+}")
    public ResponseEntity<Resource> displayPdf(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(pdfDir).resolve(filename).normalize();
            if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                return ResponseEntity.notFound().build();
            }

            org.springframework.core.io.Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/editProjectProposal/{id}")
    public String editProjectProposalForm(@PathVariable("id") Long proposalId, Model model) {
        ProjectProposal proposal = projectProposalService.getProjectProposalById(proposalId);
        model.addAttribute("proposal", proposal);
        return "project/proposalform/EditProjectProposal";
    }

    @PostMapping("/updateProjectProposal/{id}")
    public String updateProjectProposal(@PathVariable("id") Long proposalId,
                                        @ModelAttribute ProjectProposal proposal,
                                        @RequestParam("file") MultipartFile file,
                                        RedirectAttributes redirectAttributes) {
        try {
            // Save file if it's not empty and it's a valid PDF
            if (!file.isEmpty() && file.getOriginalFilename().endsWith(".pdf")) {
                String filePath = saveFile(file);
                proposal.setAttachment(filePath);  // Store the file path
            }

            proposal.setId(proposalId);  // Set the proposal ID
            projectProposalService.updateProjectProposal(proposal);  // Update the proposal in the database
            redirectAttributes.addFlashAttribute("message", "Project proposal updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error updating project proposal: " + e.getMessage());
        }
        return "redirect:/viewProjectProposals";  // Redirect to the proposal list page
    }

    @PostMapping("/deleteProposal/{id}")
    public String deleteProjectProposal(@PathVariable("id") Long proposalId, RedirectAttributes redirectAttributes) {
        try {
            projectProposalService.deleteProjectProposalById(proposalId);
            redirectAttributes.addFlashAttribute("message", "Project proposal deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting project proposal: " + e.getMessage());
        }
        return "redirect:/viewProjectProposals";
    }

    private String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();  // Unique file name
        Path path = Paths.get(pdfDir).resolve(fileName);  // Resolve file path in pdf-dir
        Files.createDirectories(path.getParent());  // Create directories if they don't exist
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);  // Copy the file
        return path.toString();  // Return the file path
    }
}
