package com.avirantEnterprises.information_collector.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.avirantEnterprises.information_collector.model.Form;
import com.avirantEnterprises.information_collector.repository.FormRepository;

@Controller
public class FormBuilderController {
    @Autowired
    private FormRepository formRepository;

    @GetMapping("/formBuilder")
    public String formBuilder(Model model) {
        model.addAttribute("form", new Form());
        return "formBuilder";
    }

    @PostMapping("/saveForm")
    public String saveForm(@ModelAttribute("form") Form form) {
        formRepository.save(form);
        return "redirect:/forms";
    }

    @GetMapping("/forms")
    public String showForms(Model model) {
        model.addAttribute("forms", formRepository.findAll());
        return "forms";
    }

    @GetMapping("/editForm/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Form form = formRepository.findById(id).orElse(new Form());
        model.addAttribute("form", form);
        return "formBuilder";
    }

    @GetMapping("/deleteForm/{id}")
    public String deleteForm(@PathVariable Long id) {
        formRepository.deleteById(id);
        return "redirect:/forms";
    }
}
