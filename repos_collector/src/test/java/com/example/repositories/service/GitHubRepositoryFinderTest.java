package com.example.repositories.service;

import com.example.repositories.model.GitHubRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubRepositoryFinderTest {
    private int daysBack = 7;
    @Mock
    private GitHubApi gitHubApi;

    @Test
    void isCurrentRepositoryInTimeRangeTrue() {
        GitHubRepository repo = new GitHubRepository();
        repo.setCreatedAt(Instant.now().minus(5, ChronoUnit.DAYS));
        when(gitHubApi.findFirstFromGitHubRepositories(0)).thenReturn(repo);

        GitHubRepositoryFinder finder = new GitHubRepositoryFinder(gitHubApi, daysBack);
        assertTrue(finder.isCurrentRepositoryInTimeRange());
    }

    @Test
    void isCurrentRepositoryInTimeRangeFalse() {
        GitHubRepository repo = new GitHubRepository();
        repo.setCreatedAt(Instant.now().minus(10, ChronoUnit.DAYS));
        when(gitHubApi.findFirstFromGitHubRepositories(0)).thenReturn(repo);

        GitHubRepositoryFinder finder = new GitHubRepositoryFinder(gitHubApi, daysBack);
        assertFalse(finder.isCurrentRepositoryInTimeRange());
    }

    @Test
    void findNextRepositoryWhenNextId() {
        GitHubRepository repo = new GitHubRepository();
        repo.setId(10);
        when(gitHubApi.findFirstFromGitHubRepositories(0)).thenReturn(repo);
        GitHubRepository next = new GitHubRepository();
        next.setId(21);
        when(gitHubApi.findFirstFromGitHubRepositories(20)).thenReturn(next);

        GitHubRepositoryFinder finder = new GitHubRepositoryFinder(gitHubApi, daysBack);

        finder.findNextRepository();
        verify(gitHubApi, times(1)).findFirstFromGitHubRepositories(0);
        verify(gitHubApi, times(1)).findFirstFromGitHubRepositories(20);
    }

    @Test
    void findNextRepositoryWhenNextIdIsTooBig() {
        GitHubRepository repo = new GitHubRepository();
        repo.setId(10);
        when(gitHubApi.findFirstFromGitHubRepositories(0)).thenReturn(repo);
        when(gitHubApi.findFirstFromGitHubRepositories(20)).thenReturn(null);
        when(gitHubApi.findFirstFromGitHubRepositories(15)).thenReturn(null);

        GitHubRepository next = new GitHubRepository();
        next.setId(11);
        when(gitHubApi.findFirstFromGitHubRepositories(12)).thenReturn(next);

        GitHubRepositoryFinder finder = new GitHubRepositoryFinder(gitHubApi, daysBack);

        finder.findNextRepository();
        verify(gitHubApi, times(1)).findFirstFromGitHubRepositories(0);
        verify(gitHubApi, times(1)).findFirstFromGitHubRepositories(20);
        verify(gitHubApi, times(1)).findFirstFromGitHubRepositories(15);
        verify(gitHubApi, times(1)).findFirstFromGitHubRepositories(12);
    }

    @Test
    void findFirstRepositoryIdInTimeRange() {
        GitHubRepository repo = new GitHubRepository();
        repo.setId(10);
        repo.setCreatedAt(Instant.now().minus(10, ChronoUnit.DAYS));
        when(gitHubApi.findFirstFromGitHubRepositories(0)).thenReturn(repo);
        when(gitHubApi.findFirstFromGitHubRepositories(5)).thenReturn(repo);
        when(gitHubApi.findFirstFromGitHubRepositories(7)).thenReturn(repo);
        when(gitHubApi.findFirstFromGitHubRepositories(8)).thenReturn(repo);

        GitHubRepository repo1 = new GitHubRepository();
        repo1.setCreatedAt(Instant.now().minus(5, ChronoUnit.DAYS));
        when(gitHubApi.findFirstFromGitHubRepositories(9)).thenReturn(repo1);

        GitHubRepositoryFinder finder = new GitHubRepositoryFinder(gitHubApi, daysBack);

        long id = finder.findFirstRepositoryIdInTimeRange();

        assertEquals(9, id);
    }
}