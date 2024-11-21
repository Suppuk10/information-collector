package com.avirantEnterprises.information_collector.controller.personal;

import com.avirantEnterprises.information_collector.service.personal.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Controller
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/registration")
    public String showRegistrationForm() {
        return "personal/registration";
    }

    @PostMapping("/register")
    public String register(@RequestParam("name") String name,
                           @RequestParam("dob") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
                           @RequestParam("contact") String contact,
                           @RequestParam("profilePic") MultipartFile profilePic) {
        registrationService.registerUser(name, dob, contact, profilePic);
        return "personal/success";
    }
}
