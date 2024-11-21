package com.avirantEnterprises.information_collector.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.avirantEnterprises.information_collector.model.Form;
import com.avirantEnterprises.information_collector.model.Response;
import com.avirantEnterprises.information_collector.repository.FormRepository;
import com.avirantEnterprises.information_collector.repository.ResponseRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ResponseController {
    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private FormRepository formRepository;

    @GetMapping("/form/{id}/response")
    public String showResponseForm(@PathVariable Long id, Model model) {
        Form form = formRepository.findById(id).orElse(new Form());
        model.addAttribute("form", form);
        model.addAttribute("response", new Response());
        return "responseForm";
    }

    @PostMapping("/form/{id}/response")
    public String saveResponse(@PathVariable Long id, @ModelAttribute Response response, @RequestParam Map<String, String> requestParams) {
        Form form = formRepository.findById(id).orElse(new Form());
        response.setForm(form);

        // Collect answers from the form submission
        Map<String, String> answers = new HashMap<>();
        for (String key : requestParams.keySet()) {
            if (key.startsWith("question_")) {
                String questionText = key.replace("question_", "");
                answers.put(questionText, requestParams.get(key));
            }
        }
        response.setAnswers(answers);

        responseRepository.save(response);
        return "redirect:/thankYou";
    }

    @GetMapping("/form/{id}/responses")
    public String viewResponses(@PathVariable Long id, Model model) {
        Form form = formRepository.findById(id).orElse(new Form());
        List<Response> responses = responseRepository.findByForm(form);
        model.addAttribute("form", form);
        model.addAttribute("responses", responses);
        return "viewResponses";
    }
    @GetMapping("/thankYou")
    public String thankYouPage() {
        return "thankYou";
    }
}
