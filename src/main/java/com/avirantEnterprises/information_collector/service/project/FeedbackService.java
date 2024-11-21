package com.avirantEnterprises.information_collector.service.project;

import com.avirantEnterprises.information_collector.model.project.Feedback;
import com.avirantEnterprises.information_collector.repository.project.FeedbackRepository;
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
public class FeedbackService {
    @Value("${file.pdf-dir}")
    private String pdfDir;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public Feedback submitFeedback(String feedbackType, String issueTitle, String description,
                                   String priority, String status, MultipartFile attachment) throws IOException {
        if (attachment.isEmpty()) {
            throw new IllegalArgumentException("Attachment cannot be empty");
        }

        // Determine file type and destination directory
        String originalFileName = attachment.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String destinationDir;

        if (isImageFile(fileExtension)) {
            destinationDir = uploadDir; // For images
        } else if ("pdf".equalsIgnoreCase(fileExtension)) {
            destinationDir = pdfDir; // For PDFs
        } else {
            throw new IllegalArgumentException("Invalid file type. Only PDFs and images are allowed.");
        }

        // Generate unique filename and create the path
        String newFileName = UUID.randomUUID().toString() + "_" + originalFileName;
        Path attachmentLocation = Paths.get(destinationDir).resolve(newFileName);

        // Create directories if they do not exist
        if (!Files.exists(attachmentLocation.getParent())) {
            Files.createDirectories(attachmentLocation.getParent());
        }

        // Save the file
        Files.copy(attachment.getInputStream(), attachmentLocation);

        // Create Feedback object
        Feedback feedback = new Feedback();
        feedback.setFeedbackType(feedbackType);
        feedback.setIssueTitle(issueTitle);
        feedback.setDescription(description);
        feedback.setPriority(priority);
        feedback.setStatus(status);
        feedback.setAttachment(attachmentLocation.toString());

        // Save and return feedback
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id).orElse(null);
    }

    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    // Helper method to extract the file extension
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new IllegalArgumentException("File name is invalid.");
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    // Helper method to check if the file is an image
    private boolean isImageFile(String fileExtension) {
        return List.of("jpg", "jpeg", "png", "gif").contains(fileExtension);
    }

    public void updateFeedback(Long feedbackId, Feedback feedback) {
    }
}
