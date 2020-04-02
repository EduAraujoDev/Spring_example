package br.com.alura.forum.controller;

import br.com.alura.forum.model.OpenTopicsByCategory;
import br.com.alura.forum.repository.OpenTopicByCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/reports")
public class ReportsController {

    @Autowired
    private OpenTopicByCategoryRepository openTopicByCategoryRepository;

    @GetMapping("/open-topics-by-category")
    public String shoeOpenTopicsByCategoryReport(Model model) {
        List<OpenTopicsByCategory> openTopics = openTopicByCategoryRepository.findAllByCurrentMonth();
        model.addAttribute("openTopics", openTopics);

        return "report";
    }
}