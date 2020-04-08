package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.input.NewAnswerInputDto;
import br.com.alura.forum.controller.dto.output.AnswerOutputDto;
import br.com.alura.forum.model.Answer;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.service.AnswerService;
import br.com.alura.forum.service.NewReplayProcessorService;
import br.com.alura.forum.service.TopicService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/topics/{topicId}/answers")
public class AnswerController {

    private TopicService topicService;
    private AnswerService answerService;
    private NewReplayProcessorService newReplayProcessorService;

    public AnswerController(TopicService topicService, AnswerService answerService, NewReplayProcessorService newReplayProcessorService) {
        this.topicService = topicService;
        this.answerService = answerService;
        this.newReplayProcessorService = newReplayProcessorService;
    }

    @CacheEvict(value = "topicDetails", key = "#topicId")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnswerOutputDto> answerTopic(@PathVariable Long topicId, @Valid @RequestBody NewAnswerInputDto newAnswerDto,
                                                       @AuthenticationPrincipal User lofferUser, UriComponentsBuilder uriBuilder) {

        Topic topic = topicService.findById(topicId);
        Answer answer = newAnswerDto.build(topic, lofferUser);

        newReplayProcessorService.execute(answer);

        URI path = uriBuilder
                .path("/api/topics/{topicId}/answers/{answer}")
                .buildAndExpand(topicId, answer.getId())
                .toUri();

        return ResponseEntity.created(path).body(new AnswerOutputDto(answer));
    }

    @Transactional
    @CacheEvict(value = "topicDetails", key = "#topicId")
    @PostMapping("/{answerId}/solution")
    public ResponseEntity<?> markAsSolution(@PathVariable Long topicId, @PathVariable Long answerId,
                                            UriComponentsBuilder uriBuilder, @AuthenticationPrincipal User loggerUser) {

        Topic topic = this.topicService.findById(topicId);

        if (loggerUser.isOwnerOf(topic) || loggerUser.isAdmin()) {
            Answer answer = this.answerService.findById(answerId);
            answer.markAsSolution();

            URI path = uriBuilder
                    .path("/api/topics/{answerId}/solution")
                    .buildAndExpand(topicId)
                    .toUri();

            return ResponseEntity.created(path).body(new AnswerOutputDto(answer));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem direito a acessar este recurso!");
    }
}