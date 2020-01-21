package com.example.repositories.service;

import com.example.repositories.model.GitHubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Helper class for finding first Id of a repository created `since` n days.
 */

public class GitHubRepositoryFinder {
    private final GitHubApi gitHubApi;
    private long startId = 0;
    private long nextId;
    private GitHubRepository repo;
    private final Instant sinceDay;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public GitHubRepositoryFinder(GitHubApi gitHubApi, int daysBack) {
        this.gitHubApi = gitHubApi;
        this.sinceDay = Instant.now().minus(daysBack, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
        this.repo = gitHubApi.findFirstFromGitHubRepositories(0);
        this.nextId = repo.getId();
    }

    /**
     * Checking if current repository was created after needed time
     *
     * @return true if current repository was created after needed time
     * false otherwise.
     */
    public boolean isCurrentRepositoryInTimeRange() {
        return repo.getCreatedAt().truncatedTo(ChronoUnit.DAYS).isAfter(sinceDay);
    }

    /**
     * Finding next repository with an id twice as much as previous repository id.
     * If there is no such repository with this new id - decreasing this new id while the repository will be found.
     * The new Id must be always bigger then the previous one.
     */
    public void findNextRepository() {
        logger.debug("Finding next repository ID. StartId = {}, NextId = {}", startId, nextId);
        repo = gitHubApi.findFirstFromGitHubRepositories(nextId * 2);
        if (repo == null) {
            long id = nextId * 2;
            while (repo == null) {
                logger.debug("There is no repository with Id {}. Reducing Id", id);
                id = (id - nextId) / 2 + nextId;
                repo = gitHubApi.findFirstFromGitHubRepositories(id);
            }
        }
        startId = nextId;
        nextId = repo.getId();
        logger.debug("New Ids range was found. StartId = {}, NextId = {}", startId, nextId);
    }


    /**
     * Finding first repository id with needed creation time.
     *
     * @return the id of founded repository
     */
    public long findFirstRepositoryIdInTimeRange() {
        logger.debug("Finding first repository id with needed creation time. StartId = {}, NextId = {}", startId, nextId);
        while (startId + 1 != nextId) {
            long middleId = (startId + nextId) / 2;
            repo = gitHubApi.findFirstFromGitHubRepositories(middleId);
            if (sinceDay.isAfter(repo.getCreatedAt().truncatedTo(ChronoUnit.DAYS))) {
                startId = middleId;
            } else {
                nextId = middleId;
            }
            logger.debug("New Ids range was found. StartId = {}, NextId = {}", startId, nextId);
        }
        logger.debug("GitHub repository was found! Id = {}", nextId);
        return nextId;
    }
}
