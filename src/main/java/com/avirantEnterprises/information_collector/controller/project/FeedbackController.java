package com.avirantEnterprises.information_collector.controller.project;

import com.avirantEnterprises.information_collector.model.project.Feedback;
import com.avirantEnterprises.information_collector.service.project.FeedbackService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
public class FeedbackController {

    @Value("${file.pdf-dir}")
    private String pdfDir;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/feedbackForm")
    public String feedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "project/feedback/FeedbackForm";
    }

    @PostMapping("/submitFeedback")
    public String submitFeedback(
            @RequestParam("feedbackType") String feedbackType,
            @RequestParam("issueTitle") String issueTitle,
            @RequestParam("description") String description,
            @RequestParam("priority") String priority,
            @RequestParam("status") String status,
            @RequestParam("attachment") MultipartFile attachment,
            Model model) {
        try {
            Feedback feedback = feedbackService.submitFeedback(feedbackType, issueTitle, description, priority, status, attachment);
            model.addAttribute("message", "Feedback submitted successfully!");
            return "project/feedback/success"; // Redirect to a success page
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error uploading file: " + e.getMessage());
            return "project/feedback/FeedbackForm"; // Return to the feedback form with error message
        }
    }

    @GetMapping("/allFeedbacks")
    public String getAllFeedbacks(Model model) {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        model.addAttribute("feedbacks", feedbacks);
        return "project/feedback/DisplayFeedback"; // Page showing all feedbacks
    }

    @GetMapping("/feedback/{id}")
    public String getFeedbackById(@PathVariable Long id, Model model) {
        Feedback feedback = feedbackService.getFeedbackById(id);
        if (feedback == null) {
            model.addAttribute("errorMessage", "Feedback not found.");
            return "project/feedback/notFound"; // Error page when feedback not found
        }
        model.addAttribute("feedback", feedback);
        return "project/feedback/view"; // Page to view a single feedback
    }

    @GetMapping("/viewFile/{filename:.+}")
    public ResponseEntity<Resource> viewFile(@PathVariable String filename) {
        try {
            // Determine file path based on extension
            Path filePath = null;
            if (filename.toLowerCase().endsWith(".pdf")) {
                filePath = Paths.get(pdfDir).resolve(filename).normalize();
            } else {
                filePath = Paths.get(uploadDir).resolve(filename).normalize();
            }

            if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                return ResponseEntity.notFound().build(); // Return 404 if file not found
            }

            Resource resource = new UrlResource(filePath.toUri());

            // Determine content type and set appropriate response headers
            String contentType = Files.probeContentType(filePath);
            HttpHeaders headers = new HttpHeaders();
            if (contentType != null && contentType.startsWith("image")) {
                headers.setContentType(MediaType.parseMediaType(contentType));
                return ResponseEntity.ok().headers(headers).body(resource);
            } else if (contentType != null && contentType.equals("application/pdf")) {
                headers.setContentType(MediaType.APPLICATION_PDF);
                return ResponseEntity.ok().headers(headers).body(resource);
            } else {
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
                return ResponseEntity.ok().headers(headers).body(resource);
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/editFeedback/{id}")
    public String editFeedbackForm(@PathVariable("id") Long feedbackId, Model model) {
        Feedback feedback = feedbackService.getFeedbackById(feedbackId);
        if (feedback == null) {
            model.addAttribute("errorMessage", "Feedback not found.");
            return "feedback/notFound"; // Return to error page
        }
        model.addAttribute("feedback", feedback);
        return "project/feedback/edit"; // Form to edit feedback
    }

    @PostMapping("/updateFeedback/{id}")
    public String updateFeedback(
            @PathVariable("id") Long feedbackId,
            @ModelAttribute Feedback feedback,
            @RequestParam("file") MultipartFile file,
            Model model) {
        try {
            if (!file.isEmpty()) {
                String filePath;
                if (file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                    filePath = saveFile(file, pdfDir);
                } else {
                    filePath = saveFile(file, uploadDir);
                }
                feedback.setAttachment(filePath);
            }
            feedbackService.updateFeedback(feedbackId, feedback);
            model.addAttribute("message", "Feedback updated successfully!");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error updating feedback: " + e.getMessage());
        }
        return "project/feedback/edit";
    }

    @PostMapping("/deleteFeedback/{id}")
    public String deleteFeedback(@PathVariable Long id, Model model) {
        try {
            feedbackService.deleteFeedback(id);
            model.addAttribute("message", "Feedback deleted successfully.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error deleting feedback: " + e.getMessage());
        }
        return "project/feedback/DisplayFeedback"; // Return to the list of all feedback
    }

    // New endpoint for downloading feedbacks as CSV
    @GetMapping("/downloadFeedbacks")
    public ResponseEntity<byte[]> downloadFeedbacks() throws IOException {
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();

        // Convert feedbacks to CSV
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (PrintWriter writer = new PrintWriter(outputStream)) {
            // Write CSV header
            writer.println("Feedback Type,Issue Title,Description,Priority,Status,Attachment");

            // Write feedback data
            for (Feedback feedback : feedbacks) {
                writer.println(
                        feedback.getFeedbackType() + "," +
                                feedback.getIssueTitle() + "," +
                                feedback.getDescription() + "," +
                                feedback.getPriority() + "," +
                                feedback.getStatus() + "," +
                                feedback.getAttachment()
                );
            }
        }

        // Prepare response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "feedbacks.csv");

        return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
    }


    private String saveFile(MultipartFile file, String directory) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Paths.get(directory).resolve(fileName);
        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path);
        return path.toString();
    }
}
