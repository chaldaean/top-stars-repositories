package com.example.repositories.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.example.repositories.model.GitHubRepository;
import com.example.repositories.repository.AppRepository;
import org.springframework.stereotype.Component;

@Component
public class Query implements GraphQLQueryResolver {
    private AppRepository appRepository;

    public Query(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public Iterable<GitHubRepository> findTopRepositories() {
        return appRepository.findTop10ByOrderByStarsDesc();
    }
}
