package br.com.alura.forum.service;

import br.com.alura.forum.model.Answer;
import br.com.alura.forum.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    public Answer findById(Long answerId) {
        return answerRepository.findById(answerId);
    }
}