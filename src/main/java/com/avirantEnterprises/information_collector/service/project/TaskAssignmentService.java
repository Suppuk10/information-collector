package com.avirantEnterprises.information_collector.service.project;
import com.avirantEnterprises.information_collector.model.project.TaskAssignment;
import com.avirantEnterprises.information_collector.repository.project.TaskAssignmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TaskAssignmentService {
    @Value("${file.pdf-dir}")
    private String pdfDir;
    private final TaskAssignmentRepository taskAssignmentRepository;

    public TaskAssignmentService(TaskAssignmentRepository taskAssignmentRepository) {
        this.taskAssignmentRepository = taskAssignmentRepository;
    }

    public TaskAssignment taskAssignment(String taskname, String description, String assigneeName,String dueDate,String priority, MultipartFile attachment) throws IOException {

        String originalFileName = attachment.getOriginalFilename();
        String newFileName = UUID.randomUUID().toString() + "_" + originalFileName;
        Path attachmentLocation = Paths.get(pdfDir).resolve(newFileName);

        if (!Files.exists(attachmentLocation.getParent())) {
            Files.createDirectories(attachmentLocation.getParent());
        }

        Files.copy(attachment.getInputStream(), attachmentLocation);
        TaskAssignment taskAssignment = new TaskAssignment();
        taskAssignment.setTaskName(taskname);
        taskAssignment.setDescription(description);
        taskAssignment.setAssigneeName(assigneeName);
        taskAssignment.setDueDate(LocalDate.parse(dueDate));
        taskAssignment.setPriority(priority);
        taskAssignment.setAttachmentPath(attachmentLocation.toString());
        return taskAssignmentRepository.save(taskAssignment);

    }
    public List<TaskAssignment> getAllTasks() {
        return taskAssignmentRepository.findAll();
    }
    public TaskAssignment getTaskById(Long id) {
        return taskAssignmentRepository.findById(id).orElse(null);
    }
    public void updateTask(TaskAssignment task) {
        taskAssignmentRepository.save(task);
    }
    public void deleteTaskById(Long id) {
        taskAssignmentRepository.deleteById(id);
    }
}
