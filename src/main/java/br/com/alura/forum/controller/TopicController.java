package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.input.NewTopicInputDto;
import br.com.alura.forum.controller.dto.input.TopicSearchInputDto;
import br.com.alura.forum.controller.dto.output.DashboardDto;
import br.com.alura.forum.controller.dto.output.TopicBriefOutputDto;
import br.com.alura.forum.controller.dto.output.TopicOutputDto;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.service.DashboardService;
import br.com.alura.forum.service.TopicService;
import br.com.alura.forum.validator.NewTopicCustomValidator;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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

    @Cacheable("dashboardData")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DashboardDto> dashboard() {
        return dashboardService.findAllDashboard();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TopicOutputDto> createTopic(@Valid  @RequestBody NewTopicInputDto newTopicInputDto,
                                      @AuthenticationPrincipal User loggedUser, UriComponentsBuilder uriBuilder) {

        Topic topic = topicService.createTopic(newTopicInputDto, loggedUser);

        return ResponseEntity
                .created(uriBuilder.path("/api/topics/{id}").buildAndExpand(topic.getId()).toUri())
                .body(new TopicOutputDto(topic));
    }

    @Cacheable(value = "topicDetails", key = "#id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TopicOutputDto getTopicDetails(@PathVariable Long id) {
        return new TopicOutputDto(topicService.findById(id));
    }

    @Transactional
    @PostMapping(value = "/{id}/close")
    @CacheEvict(value = "topicDetails", key = "#id")
    public ResponseEntity<?> closeTopic(@PathVariable Long id, UriComponentsBuilder uriBuilder,
                                           @AuthenticationPrincipal User loggerUser) {

        Topic topic = topicService.findById(id);

        if (loggerUser.isOwnerOf(topic) || loggerUser.isAdmin()) {
            topic.close();

            URI path = uriBuilder
                    .path("/api/topics/{answerId}/solution")
                    .buildAndExpand(topic.getId())
                    .toUri();

            return ResponseEntity.noContent().location(path).build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem direito a acessar este recurso!");
    }

    @InitBinder("newTopicInputDto")
    public void initBinder(WebDataBinder binder, @AuthenticationPrincipal User loggedUser) {
        binder.addValidators(new NewTopicCustomValidator(topicService, loggedUser));
    }
}