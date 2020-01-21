package com.example.repositories.service;

import com.example.repositories.model.GitHubRepositories;
import com.example.repositories.model.GitHubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for calling GitHub API
 */

@Service
public class GitHubApi {
    private final RestTemplate restTemplate;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public GitHubApi(RestTemplateBuilder builder, @Value("${github.user}") String user, @Value("${github.token}") String token) {
        this.restTemplate = builder.basicAuthentication(user, token).build();
    }

    /**
     * List of first 100 public repositories in the order that they were created.
     *
     * @param since The integer ID of the Repository to start look from.
     *              This repository is not included to the result list
     * @return List of 100 public repositories ordered by creation date.
     * Can return empty list if there is no public repositories created after for this repository ID
     */
    public List<GitHubRepositories> getGitHubRepositories(long since) {
        String apiUrl = "https://api.github.com/repositories?since=" + since;
        ResponseEntity<ArrayList<GitHubRepositories>> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<ArrayList<GitHubRepositories>>() {
                });
        return response.getBody();
    }

    /**
     * GitHub repository information
     *
     * @param url GitHub API url for repository
     * @return Detailed information about repository
     */
    public GitHubRepository getGitHubRepository(String url) {
        try {
            ResponseEntity<GitHubRepository> response = restTemplate.exchange(url, HttpMethod.GET, null, GitHubRepository.class);
            return response.getBody();
        } catch (RestClientResponseException ex) {
            logger.error(ex.getMessage());
            logger.error("There is no repository with url: {} ", url);
            return null;
        }
    }


    /**
     * Get GitHub repository information for the first repository from the list of public repositories.
     * If the list is empty (there is no public repositories created after for this repository ID), return null.
     *
     * @param since The integer ID of the Repository to start look from.
     *              This repository is not included to the result list
     * @return Detailed information about repository
     */
    public GitHubRepository findFirstFromGitHubRepositories(long since) {
        List<GitHubRepositories> repositories = getGitHubRepositories(since);
        for (GitHubRepositories r : repositories) {
            GitHubRepository gitHubRepository = getGitHubRepository(r.getUrl());
            if (gitHubRepository != null) {
                return gitHubRepository;
            }
        }
        return null;
    }

    /**
     * Get GitHub's detailed information for all repositories from the list.
     *
     * @param since The integer ID of the Repository to start look from.
     *              This repository is not included to the result list
     * @return List with detailed information about all repositories
     * Can return empty list if there is no public repositories created after for this repository ID
     */
    public List<GitHubRepository> findAllFromGitHubRepositories(long since) {
        List<GitHubRepository> result = new ArrayList<>();
        List<GitHubRepositories> repositories = getGitHubRepositories(since);
        for (GitHubRepositories gitRepo : repositories) {
            GitHubRepository r = getGitHubRepository(gitRepo.getUrl());
            if (r != null) {
                result.add(r);
            }
        }
        return result;
    }
}
