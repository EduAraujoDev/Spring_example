package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.input.NewAnswerInputDto;
import br.com.alura.forum.controller.dto.output.AnswerOutputDto;
import br.com.alura.forum.exception.ResourceNotFoundException;
import br.com.alura.forum.model.Answer;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.TopicRepository;
import br.com.alura.forum.service.NewReplayProcessorService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/topics/{topicId}/answers")
public class AnswerController {

    private TopicRepository topicRepository;
    private NewReplayProcessorService newReplayProcessorService;

    public AnswerController(TopicRepository topicRepository, NewReplayProcessorService newReplayProcessorService) {
        this.topicRepository = topicRepository;
        this.newReplayProcessorService = newReplayProcessorService;
    }

    @CacheEvict(value = "topicDetails", key = "#topicId")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnswerOutputDto> answerTopic(@PathVariable Long topicId, @Valid @RequestBody NewAnswerInputDto newAnswerDto,
                                                       @AuthenticationPrincipal User lofferUser, UriComponentsBuilder uriBuilder) {

        Topic topic = topicRepository.findById(topicId).orElseThrow(ResourceNotFoundException::new);
        Answer answer = newAnswerDto.build(topic, lofferUser);

        newReplayProcessorService.execute(answer);

        URI path = uriBuilder
                .path("/api/topics/{topicId}/answers/{answer}")
                .buildAndExpand(topicId, answer.getId())
                .toUri();

        return ResponseEntity.created(path).body(new AnswerOutputDto(answer));
    }
}