package br.com.alura.forum.controller;

import br.com.alura.forum.dto.input.TopicSearchInputDto;
import br.com.alura.forum.dto.output.DashboardDto;
import br.com.alura.forum.dto.output.TopicBriefOutputDto;
import br.com.alura.forum.model.Category;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.CategoryRepository;
import br.com.alura.forum.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<TopicBriefOutputDto> listTopics(TopicSearchInputDto topicSearch, Pageable pageRequest) {

        Specification<Topic> topicSearchSpecification = topicSearch.build();
        Page<Topic> topics = this.topicRepository.findAll(topicSearchSpecification, pageRequest);

        return TopicBriefOutputDto.listFromTopics(topics);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DashboardDto> dashboard() {
        List<DashboardDto> dashboards = new ArrayList<>();

        List<Category> categories = categoryRepository.findByCategoryIsNull();

        for (Category category : categories) {
            int alltopics = topicRepository.countTopicsByCategory(category);
            int lastWeekTopics = topicRepository.countLastWeekTopicsByCategory(
                    category, Instant.now().minus(Duration.ofDays(7)));
            int unansweredTopics = topicRepository.countUnansweredTopicsByCategory(category);
            DashboardDto dto = new DashboardDto(category, alltopics, lastWeekTopics, unansweredTopics);

            dashboards.add(dto);
        }
        
        return dashboards;
    }
}