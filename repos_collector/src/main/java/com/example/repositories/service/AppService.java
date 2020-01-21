package com.example.repositories.service;

import com.example.repositories.event.SavingRepoEvent;
import com.example.repositories.event.ScheduleEvent;
import com.example.repositories.event.StartFindingRepoEvent;
import com.example.repositories.model.GitHubRepository;
import com.example.repositories.repository.AppRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppService {
    @Value("${github.daysback}")
    private int daysBack;

    private final ApplicationEventPublisher eventPublisher;

    private final GitHubApi gitHubApi;
    private final AppRepository appRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public AppService(GitHubApi gitHubApi, AppRepository appRepository, ApplicationEventPublisher eventPublisher) {
        this.gitHubApi = gitHubApi;
        this.appRepository = appRepository;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedRate = 7200000) // One hour
    public void startSavingGitHubRepositories() {
        logger.info("Starting collecting GitHub repositories `since` {} days.", daysBack);
        eventPublisher.publishEvent(new ScheduleEvent(this));
    }

    @EventListener
    public void findLastSavedGitHubRepositoryId(ScheduleEvent scheduleEvent) {
        GitHubRepository latestRecord = appRepository.findTopByOrderByIdDesc();
        long startRepoId = latestRecord != null ? latestRecord.getId() : 0;
        eventPublisher.publishEvent(new StartFindingRepoEvent(this, startRepoId));
    }

    @EventListener
    public void handleContextStart(StartFindingRepoEvent startFindingRepoEvent) {
        long repoId = startFindingRepoEvent.getStartRepoId();
        if (repoId == 0) {
            logger.info("There no saved repositories in DB. Starting to find from the first GitHub repository...");
            repoId = findRepositoryIdSinceTime(daysBack);
        }
        eventPublisher.publishEvent(new SavingRepoEvent(this, repoId));
    }

    @EventListener
    public void startSavingGitHubRepositories(SavingRepoEvent event) {
        long repoId = event.getRepoId();
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
