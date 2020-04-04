package br.com.alura.forum;

import br.com.alura.forum.security.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class ForumApplicationTests {

	@Autowired
	private UserService userService;

	@Test
	void contextLoads() {
		assertThat(userService).isNotNull();
		assertThat(userService).isInstanceOf(UserDetailsService.class);
	}
}