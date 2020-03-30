package br.com.alura.forum.service;

import br.com.alura.forum.mail.ForumMailService;
import br.com.alura.forum.model.Answer;
import br.com.alura.forum.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewReplayProcessorService {

    private AnswerRepository answerRepository;
    private ForumMailService forumMailService;

    public NewReplayProcessorService(AnswerRepository answerRepository, ForumMailService forumMailService) {
        this.answerRepository = answerRepository;
        this.forumMailService = forumMailService;
    }

    public void execute(Answer answer) {
        this.answerRepository.save(answer);
        this.forumMailService.sendNewReplayMail(answer);
    }
}