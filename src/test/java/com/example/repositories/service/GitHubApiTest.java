package com.example.repositories.service;

import com.example.repositories.model.GitHubRepositories;
import com.example.repositories.model.GitHubRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class GitHubApiTest {
    @Autowired
    private GitHubApi gitHubApi;

    @Test
    void getGitHubRepositories() {
        List<GitHubRepositories> gitHubRepositories = gitHubApi.getGitHubRepositories(0);
        assertFalse(gitHubRepositories.isEmpty());
        assertEquals(100, gitHubRepositories.size());
    }

    @Test
    void getEmptyListWhenThereNoMoreRepositories() {
        List<GitHubRepositories> gitHubRepositories = gitHubApi.getGitHubRepositories(90000000000L);
        assertTrue(gitHubRepositories.isEmpty());
    }

    @Test
    void getGitHubRepository() {
        String url = "https://api.github.com/repos/mojombo/grit";
        GitHubRepository gitHubRepository = gitHubApi.getGitHubRepository(url);
        assertNotNull(gitHubRepository);
    }

    @Test
    void getNullWhenNoGitHubRepositoryByUrl() {
        String url = "https://api.github.com/repos/null";
        GitHubRepository gitHubRepository = gitHubApi.getGitHubRepository(url);
        assertNull(gitHubRepository);
    }

    @Test
    void findFirstFromGitHubRepositories() {
        GitHubRepository first = gitHubApi.findFirstFromGitHubRepositories(100);
        assertNotNull(first);

        List<GitHubRepositories> gitHubRepositories = gitHubApi.getGitHubRepositories(100);
        assertEquals(gitHubRepositories.get(0).getId(), first.getId());
    }

    @Test
    void findAllFromGitHubRepositories() {
        List<GitHubRepository> allFromGitHubRepositories = gitHubApi.findAllFromGitHubRepositories(0);
        assertFalse(allFromGitHubRepositories.isEmpty());
        assertEquals(100, allFromGitHubRepositories.size());
    }
}