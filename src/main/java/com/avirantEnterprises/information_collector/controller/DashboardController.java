package com.avirantEnterprises.information_collector.controller;

import com.avirantEnterprises.information_collector.model.Userr;
import com.avirantEnterprises.information_collector.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("username", "User's Name"); // Pass dynamic data if needed
        return "dashboard"; // Renders dashboard.html
    }
}

