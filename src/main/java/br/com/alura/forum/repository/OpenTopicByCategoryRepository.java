package br.com.alura.forum.repository;

import br.com.alura.forum.model.OpenTopicsByCategory;
import org.springframework.data.repository.Repository;

public interface OpenTopicByCategoryRepository extends Repository<OpenTopicsByCategory, Long> {

    void saveAll(Iterable<OpenTopicsByCategory> topics);
}