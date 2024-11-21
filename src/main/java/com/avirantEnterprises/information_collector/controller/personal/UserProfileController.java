package com.avirantEnterprises.information_collector.controller.personal;

import com.avirantEnterprises.information_collector.model.personal.User;
import com.avirantEnterprises.information_collector.repository.personal.UserRepository;
import com.avirantEnterprises.information_collector.service.personal.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Controller
public class UserProfileController {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile/{id}")
    public String viewUserProfile(@PathVariable Long id, Model model) {
        User user = registrationService.getUserById(id);
        //System.out.println("Profile Picture Path: " + user.getProfilePicPath());
        model.addAttribute("user", user);
        return "personal/profile_view"; // View template for displaying the user profile
    }
    @GetMapping("/profile/delete/{id}")
    public String deleteUserProfile(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/personal/profile_list"; // Redirect to a list of profiles or another appropriate page
    }
    @GetMapping("/profileList")
    public String listUserProfiles(Model model) {
        List<User> users = registrationService.getAllUsers();
        model.addAttribute("users", users);
        return "personal/profile_list";
    }
    @GetMapping("/profile/update/{id}")
    public String showUpdateProfileForm(@PathVariable Long id, Model model) {
        User user = registrationService.getUserById(id);
        model.addAttribute("user", user);
        return "personal/profile_update"; // Reference the correct path to your template
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@RequestParam("id") Long id,
                                @RequestParam("name") String name,
                                @RequestParam("dob") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
                                @RequestParam("contact") String contact,
                                @RequestParam("profilePic") MultipartFile profilePic) {
        registrationService.updateUserProfile(id, name, dob, contact, profilePic);
        return "redirect:/profile/" + id; // Redirect to the updated profile page
    }
}
