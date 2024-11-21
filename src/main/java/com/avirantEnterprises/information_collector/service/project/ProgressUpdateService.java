package com.avirantEnterprises.information_collector.service.project;

import com.avirantEnterprises.information_collector.model.project.ProgressUpdate;
import com.avirantEnterprises.information_collector.repository.project.ProgressUpdateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ProgressUpdateService {

    @Autowired
    private ProgressUpdateRepository repository;

    private final String uploadDir = "uploads/";

    public void saveProgressUpdate(String projectName, String milestoneUpdate, MultipartFile progressFile) throws IOException {
        ProgressUpdate progressUpdate = new ProgressUpdate();
        progressUpdate.setProjectName(projectName);
        progressUpdate.setMilestoneUpdate(milestoneUpdate);

        // Ensure the uploads directory exists
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            boolean dirCreated = uploadDirectory.mkdirs();
            if (!dirCreated) {
                throw new IOException("Could not create upload directory.");
            }
        }

        if (progressFile != null && !progressFile.isEmpty()) {
            try {
                // Save the file to the uploads directory
                String filePath = uploadDir + progressFile.getOriginalFilename();
                File file = new File(filePath);
                progressFile.transferTo(file);
                progressUpdate.setProgressFilePath(filePath);
            } catch (IOException e) {
                // Log the error and proceed
                e.printStackTrace();
                progressUpdate.setProgressFilePath(null);
                throw new IOException("File upload failed: " + e.getMessage());
            }
        } else {
            progressUpdate.setProgressFilePath(null);
        }

        repository.save(progressUpdate);
    }
}
