package com.avirantEnterprises.information_collector.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.avirantEnterprises.information_collector.model.Data;
import com.avirantEnterprises.information_collector.service.DataService;

@Controller
public class DataController {
    @Autowired
    private DataService dataService;

    @GetMapping("/data")
    public String data(Model model) {
        model.addAttribute("dataList", dataService.findAllData());
        return "data";
    }

    @GetMapping("/registerData")
    public String registerData(Model model) {
        Data data = new Data();
        model.addAttribute("data", data);
        return "registerData";
    }

    @PostMapping("/saveData")
    public String saveData(@ModelAttribute("data") Data data) {
        dataService.saveData(data);
        return "redirect:/data";
    }

    @GetMapping("/updateData/{id}")
    public String updateData(Model model, @PathVariable Long id) {
        Data data = dataService.getDataById(id);
        model.addAttribute("data", data);
        return "updateFormData";
    }

    @GetMapping("/deleteData/{id}")
    public String deleteData(@PathVariable Long id) {
        dataService.deleteData(id);
        return "redirect:/data";
    }
}
