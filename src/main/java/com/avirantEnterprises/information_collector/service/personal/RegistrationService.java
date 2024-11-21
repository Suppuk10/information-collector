package com.avirantEnterprises.information_collector.service.personal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.avirantEnterprises.information_collector.model.personal.User;
import com.avirantEnterprises.information_collector.repository.personal.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    private final Path rootLocation = Paths.get("upload-dir");

    public void registerUser(String name, LocalDate dob, String contact, MultipartFile profilePic) {
        User user = new User();
        user.setName(name);
        user.setDob(dob);
        user.setContact(contact);
        String profilePicPath = saveFile(profilePic);
        user.setProfilePicPath(profilePicPath);
        userRepository.save(user);
    }

    public void updateUserProfile(Long id, String name, LocalDate dob, String contact, MultipartFile profilePic) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(name);
            user.setDob(dob);
            user.setContact(contact);
            if (!profilePic.isEmpty()) {
                String profilePicPath = saveFile(profilePic);
                user.setProfilePicPath(profilePicPath);
            }
            userRepository.save(user);
        }
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



//    private String saveFile(MultipartFile file) {
//        try {
//            Files.createDirectories(rootLocation);
//            Path destinationFile = rootLocation.resolve(Paths.get(file.getOriginalFilename()))
//                    .normalize().toAbsolutePath();
//            file.transferTo(destinationFile);
//            return file.getOriginalFilename();  // Store only the file name
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to store file.", e);
//        }
//    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }

    private String saveFile(MultipartFile file) {
        try {
            Files.createDirectories(rootLocation);
            String sanitizedFileName = sanitizeFileName(file.getOriginalFilename());
            Path destinationFile = rootLocation.resolve(Paths.get(sanitizedFileName))
                    .normalize().toAbsolutePath();
            file.transferTo(destinationFile);
            return sanitizedFileName;  // Store only the sanitized file name
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }




}
