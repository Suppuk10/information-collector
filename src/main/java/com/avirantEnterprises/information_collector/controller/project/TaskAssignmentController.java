package com.avirantEnterprises.information_collector.controller.project;

import com.avirantEnterprises.information_collector.model.project.TaskAssignment;
import com.avirantEnterprises.information_collector.service.project.TaskAssignmentService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
public class TaskAssignmentController {
    @Autowired
    private TaskAssignmentService taskAssignmentService;
    @Value("${file.pdf-dir}")
    private String pdfDir;

    @GetMapping("/taskAssignForm")
    public String taskAssignForm(Model model) {
        model.addAttribute("taskAssignment", new TaskAssignment());
        return "project/TaskAssignment";
    }

    @PostMapping("/taskAssign")
    public String createTaskAssignment(@RequestParam("taskName") String taskName,
                                       @RequestParam("description") String description,
                                       @RequestParam("assigneeName") String assigneeName,
                                       @RequestParam("dueDate") String dueDate,
                                       @RequestParam("priority") String priority,
                                       @RequestParam("attachment") MultipartFile attachment, Model model) {
        try {
            taskAssignmentService.taskAssignment(taskName, description, assigneeName, String.valueOf(LocalDate.parse(dueDate)), priority, attachment);
            model.addAttribute("message", "Task assigned successfully!");
            return "project/success";
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Could not upload attachment: " + e.getMessage());
            return "project/TaskAssignment";
        }
    }

    @GetMapping("/tasks")
    public String viewAllTasks(Model model) {
        List<TaskAssignment> tasks = taskAssignmentService.getAllTasks();

        // Clean attachment paths (optional: do this in the service itself)
        tasks.forEach(task -> {
            if (task.getAttachmentPath() != null) {
                String fullPath = task.getAttachmentPath();
                String filename = Paths.get(fullPath).getFileName().toString(); // Extract the filename
                task.setAttachmentPath(filename); // Replace with the cleaned filename
            }
        });

        model.addAttribute("tasks", tasks);
        return "project/DisplayTask";
    }

    @GetMapping("/viewPdf/{filename:.+}")
    public ResponseEntity<org.springframework.core.io.Resource> displayPdf(@PathVariable String filename) {
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


    @GetMapping("/editTask/{id}")
    public String editTaskForm(@PathVariable("id") Long taskId, Model model) {
        TaskAssignment task = taskAssignmentService.getTaskById(taskId);
        model.addAttribute("task", task);
        return "project/EditTask";
    }


    @PostMapping("/updateTask/{id}")
    public String updateTask(@PathVariable("id") Long taskId,
                             @ModelAttribute TaskAssignment task,
                             @RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) {
        try {
            // Save file if it's not empty and it's a valid PDF
            if (!file.isEmpty() && file.getOriginalFilename().endsWith(".pdf")) {
                String filePath = saveFile(file);
                task.setAttachmentPath(filePath);  // Store the file path
            }

            task.setId(taskId);  // Set the task ID
            taskAssignmentService.updateTask(task);  // Update the task in the database
            redirectAttributes.addFlashAttribute("message", "Task updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error updating task: " + e.getMessage());
        }
        return "redirect:/tasks";  // Redirect to the task list page
    }



    @PostMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable("id") Long taskId, RedirectAttributes redirectAttributes) {
        try {
            taskAssignmentService.deleteTaskById(taskId);
            redirectAttributes.addFlashAttribute("message", "Task deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting task: " + e.getMessage());
        }
        return "redirect:/tasks";
    }




    private String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();  // Unique file name
        Path path = Paths.get(pdfDir).resolve(fileName);  // Resolve file path in pdf-dir
        Files.createDirectories(path.getParent());  // Create directories if they don't exist
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);  // Copy the file
        return path.toString();  // Return the file path
    }
}

