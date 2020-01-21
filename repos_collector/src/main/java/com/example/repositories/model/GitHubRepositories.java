package com.example.repositories.model;

import java.util.Objects;

public class GitHubRepositories {
    private long id;
    private String url;

    public GitHubRepositories(long id, String url) {
        this.id = id;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitHubRepositories that = (GitHubRepositories) o;
        return id == that.id &&
                url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }
}
