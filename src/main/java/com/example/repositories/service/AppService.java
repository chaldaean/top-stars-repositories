package com.example.repositories.service;

import com.example.repositories.model.GitHubRepository;
import com.example.repositories.repository.AppRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppService {
    @Value("${github.daysback}")
    private int daysBack;

    private final GitHubApi gitHubApi;
    private final AppRepository appRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public AppService(GitHubApi gitHubApi, AppRepository appRepository) {
        this.gitHubApi = gitHubApi;
        this.appRepository = appRepository;
    }

    //    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(fixedRate = 7200000)
    public void startSavingGitHubRepositories() {
        logger.info("Starting collecting GitHub repositories `since` {} days.", daysBack);
        long repoId = findLastSavedGitHubRepositoryId();
        if (repoId == 0) {
            logger.info("There no saved repositories in DB. Starting to find from the first GitHub repository...");
            repoId = findRepositoryIdSinceTime(daysBack);
        }
        startSavingGitHubRepositories(repoId);
    }

    private long findLastSavedGitHubRepositoryId() {
        GitHubRepository latestRecord = appRepository.findTopByOrderByIdDesc();
        return latestRecord != null ? latestRecord.getId() : 0;
    }

    private void startSavingGitHubRepositories(long repoId) {
        List<GitHubRepository> repositories;
        logger.info("Saving repositories into DB...");
        do {
            repositories = gitHubApi.findAllFromGitHubRepositories(repoId);
            if (!repositories.isEmpty()) {
                appRepository.saveAll(repositories);
                repoId = repositories.get(repositories.size() - 1).getId();
            }
        } while (!repositories.isEmpty());
        logger.info("Saving repositories finished!");
    }

    private long findRepositoryIdSinceTime(int daysBack) {
        GitHubRepositoryFinder sr = new GitHubRepositoryFinder(gitHubApi, daysBack);
        while (!sr.isCurrentRepositoryInTimeRange()) {
            sr.findNextRepository();
        }
        return sr.findFirstRepositoryIdInTimeRange();
    }
}
