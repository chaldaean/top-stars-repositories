package com.example.repositories.event;

import org.springframework.context.ApplicationEvent;

public class SavingRepoEvent extends ApplicationEvent {
    private final long repoId;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     * @param repoId GitHub repository Id to save to DB from
     */
    public SavingRepoEvent(Object source, long repoId) {
        super(source);
        this.repoId = repoId;
    }

    public long getRepoId() {
        return repoId;
    }
}
