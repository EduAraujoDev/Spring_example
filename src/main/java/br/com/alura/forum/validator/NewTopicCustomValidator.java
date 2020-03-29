package br.com.alura.forum.validator;

import br.com.alura.forum.controller.dto.input.NewTopicInputDto;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.service.TopicService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class NewTopicCustomValidator implements Validator {
    private static final int LIMIT_TOPICS_PER_HOUR = 4;

    private TopicService topicService;
    private User loggerUser;

    public NewTopicCustomValidator(TopicService topicService, User loggerUser) {
        this.topicService = topicService;
        this.loggerUser = loggerUser;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return NewTopicInputDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Instant oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS);
        List<Topic> topics = topicService.findByOwnerAndCreationInstantAfterOrderByCreationInstantAsc(loggerUser, oneHourAgo);

        if (topics.size() >= LIMIT_TOPICS_PER_HOUR) {
            Instant instantOftheOldestTopic = topics.get(0).getCreationInstant();
            long minutesToNextTopic = Duration.between(oneHourAgo, instantOftheOldestTopic).getSeconds() / 60;

            errors.reject("newTopicInputDto.limit.exceeded", new Object[] {minutesToNextTopic},
                    "O limite individual de novos tópicos por hora foi excedido");
        }
    }
}