package br.com.alura.forum.service;

import br.com.alura.forum.controller.dto.input.NewTopicInputDto;
import br.com.alura.forum.controller.dto.output.TopicOutputDto;
import br.com.alura.forum.exception.ResourceNotFoundException;
import br.com.alura.forum.model.Course;
import br.com.alura.forum.model.User;
import br.com.alura.forum.model.topic.domain.Topic;
import br.com.alura.forum.repository.CourseRepository;
import br.com.alura.forum.repository.TopicRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TopicService {

    private TopicRepository topicRepository;
    private CourseRepository courseRepository;

    public TopicService(TopicRepository topicRepository, CourseRepository courseRepository) {
        this.topicRepository = topicRepository;
        this.courseRepository = courseRepository;
    }

    public Page<Topic> findAll(Specification<Topic> topicSpecification, Pageable pageable) {
        return topicRepository.findAll(topicSpecification, pageable);
    }

    public Topic createTopic(NewTopicInputDto newTopicInputDto, User loggedUser) {
        Course course = courseRepository
                .findByName(newTopicInputDto.getCourseName())
                .orElseThrow(ResourceNotFoundException::new);

        Topic topic = new Topic(
                newTopicInputDto.getShortDescription(),
                newTopicInputDto.getContent(), loggedUser, course);

        return topicRepository.save(topic);
    }

    public List<Topic> findByOwnerAndCreationInstantAfterOrderByCreationInstantAsc(User loggerUser, Instant onehourAgo) {
        return topicRepository.findByOwnerAndCreationInstantAfterOrderByCreationInstantAsc(loggerUser, onehourAgo);
    }

    public TopicOutputDto findById(Long id) {
        return new TopicOutputDto(topicRepository.findById(id).orElseThrow(ResourceNotFoundException::new));
    }
}