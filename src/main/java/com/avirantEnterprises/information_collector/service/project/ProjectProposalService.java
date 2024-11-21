package com.avirantEnterprises.information_collector.service.project;

import com.avirantEnterprises.information_collector.model.project.ProjectProposal;
import com.avirantEnterprises.information_collector.repository.project.ProjectProposalRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectProposalService {
    @Value("${file.pdf-dir}")
    private String pdfDir;

    private final ProjectProposalRepository projectProposalRepository;

    public ProjectProposalService(ProjectProposalRepository projectProposalRepository) {
        this.projectProposalRepository = projectProposalRepository;
    }

    // Method to create a new project proposal
    public ProjectProposal createProjectProposal(String projectTitle, String projectDescription, String department,
                                                 String startDate, Double estimatedBudget, MultipartFile attachment) throws IOException {
        // Generate a new file name
        String originalFileName = attachment.getOriginalFilename();
        String newFileName = UUID.randomUUID().toString() + "_" + originalFileName;
        Path attachmentLocation = Paths.get(pdfDir).resolve(newFileName);

        // Create directories if not exist
        if (!Files.exists(attachmentLocation.getParent())) {
            Files.createDirectories(attachmentLocation.getParent());
        }

        // Copy the file to the server
        Files.copy(attachment.getInputStream(), attachmentLocation);

        // Create a new ProjectProposal object
        ProjectProposal projectProposal = new ProjectProposal();
        projectProposal.setProjectTitle(projectTitle);
        projectProposal.setProjectDescription(projectDescription);
        projectProposal.setDepartment(department);
        projectProposal.setStartDate(startDate);
        projectProposal.setEstimatedBudget(estimatedBudget);
        projectProposal.setAttachment(attachmentLocation.toString()); // Save the file path

        // Save and return the new project proposal
        return projectProposalRepository.save(projectProposal);
    }

    // Method to get all project proposals
    public List<ProjectProposal> getAllProjectProposals() {
        return projectProposalRepository.findAll();
    }

    // Method to get a project proposal by ID
    public ProjectProposal getProjectProposalById(Long id) {
        return projectProposalRepository.findById(id).orElse(null);  // Return null if not found
    }

    // Method to update an existing project proposal
    public void updateProjectProposal(ProjectProposal projectProposal) {
        projectProposalRepository.save(projectProposal);  // Save updated proposal
    }

    // Method to delete a project proposal by ID
    public void deleteProjectProposalById(Long id) {
        projectProposalRepository.deleteById(id);
    }

}
