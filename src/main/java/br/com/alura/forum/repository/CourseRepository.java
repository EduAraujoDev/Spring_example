package br.com.alura.forum.repository;

import br.com.alura.forum.model.Course;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CourseRepository extends Repository<Course, Long> {

    Optional<Course> findByName(String name);
}