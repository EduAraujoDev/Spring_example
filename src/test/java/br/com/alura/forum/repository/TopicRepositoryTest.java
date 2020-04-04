package br.com.alura.forum.repository;

import br.com.alura.forum.model.topic.domain.Topic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class TopicRepositoryTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void shouldSaveTopic() {
        Topic topic = new Topic("Descrição do tópico", "Conteúdo do tópico", null, null);

        Topic persistedTopic = this.topicRepository.save(topic);
        Topic topicoEncontrado = this.testEntityManager.find(Topic.class, persistedTopic.getId());

        assertThat(topicoEncontrado).isNotNull();
        assertThat(topicoEncontrado.getShortDescription()).isEqualTo(persistedTopic.getShortDescription());
    }
}