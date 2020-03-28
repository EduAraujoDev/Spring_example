package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.input.TopicSearchInputDto;
import br.com.alura.forum.controller.dto.output.DashboardDto;
import br.com.alura.forum.controller.dto.output.TopicBriefOutputDto;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.service.DashboardService;
import br.com.alura.forum.service.TopicService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private TopicService topicService;
    private DashboardService dashboardService;

    public TopicController(TopicService topicService, DashboardService dashboardService) {
        this.topicService = topicService;
        this.dashboardService = dashboardService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<TopicBriefOutputDto> listTopics(TopicSearchInputDto topicSearch, Pageable pageRequest) {

        Specification<Topic> topicSearchSpecification = topicSearch.build();
        Page<Topic> topics = topicService.findAll(topicSearchSpecification, pageRequest);

        return TopicBriefOutputDto.listFromTopics(topics);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DashboardDto> dashboard() {
        return dashboardService.findAllDashboard();
    }
}