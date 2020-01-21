package com.example.toprepo.repository;

import com.example.toprepo.model.GitHubRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AppRepository extends CrudRepository<GitHubRepository, Long> {
    List<GitHubRepository> findTop10ByOrderByStarsDesc();
}
