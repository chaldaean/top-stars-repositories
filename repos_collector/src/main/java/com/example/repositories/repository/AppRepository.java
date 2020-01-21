package com.example.repositories.repository;

import com.example.repositories.model.GitHubRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AppRepository extends CrudRepository<GitHubRepository, Long> {
    GitHubRepository findTopByOrderByIdDesc();

    List<GitHubRepository> findTop10ByOrderByStarsDesc();
}
