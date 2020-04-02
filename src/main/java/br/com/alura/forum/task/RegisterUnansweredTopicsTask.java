package br.com.alura.forum.task;

import br.com.alura.forum.model.OpenTopicsByCategory;
import br.com.alura.forum.repository.OpenTopicByCategoryRepository;
import br.com.alura.forum.repository.TopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegisterUnansweredTopicsTask {

    private final static Logger LOGGER = LoggerFactory.getLogger(RegisterUnansweredTopicsTask.class);

    private TopicRepository topicRepository;
    private OpenTopicByCategoryRepository openTopicByCategoryRepository;

    public RegisterUnansweredTopicsTask(TopicRepository topicRepository, OpenTopicByCategoryRepository openTopicByCategoryRepository) {
        this.topicRepository = topicRepository;
        this.openTopicByCategoryRepository = openTopicByCategoryRepository;
    }

    // @Scheduled(cron = "*/10 * * * * *")
    @Scheduled(fixedDelay = 10000)
    public void execute() {
        LOGGER.info("Executando schedule!");

        List<OpenTopicsByCategory> topics = topicRepository.findOpenTopicsByCategory();
        openTopicByCategoryRepository.saveAll(topics);
    }
}