package br.com.alura.forum.repository;

import br.com.alura.forum.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByCategoryIsNull();
}