package br.com.alura.forum.service;

import br.com.alura.forum.controller.dto.output.DashboardDto;
import br.com.alura.forum.repository.CategoryRepository;
import br.com.alura.forum.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private TopicRepository topicRepository;
    private CategoryRepository categoryRepository;

    public DashboardService(TopicRepository topicRepository, CategoryRepository categoryRepository) {
        this.topicRepository = topicRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<DashboardDto> findAllDashboard() {
        List<DashboardDto> dashboards = new ArrayList<>();

        categoryRepository.findByCategoryIsNull().forEach(category -> {
            int alltopics = topicRepository.countTopicsByCategory(category);
            int lastWeekTopics = topicRepository.countLastWeekTopicsByCategory(
                    category, Instant.now().minus(Duration.ofDays(7)));
            int unansweredTopics = topicRepository.countUnansweredTopicsByCategory(category);
            DashboardDto dto = new DashboardDto(category, alltopics, lastWeekTopics, unansweredTopics);

            dashboards.add(dto);
        });

        return dashboards;
    }
}