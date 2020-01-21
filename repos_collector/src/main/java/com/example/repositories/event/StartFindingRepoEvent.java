package com.example.repositories.event;

import org.springframework.context.ApplicationEvent;

public class StartFindingRepoEvent extends ApplicationEvent {
    private final long startRepoId;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source      the object on which the event initially occurred or with
     *                    which the event is associated (never {@code null})
     * @param startRepoId GitHub repository Id to start searching from
     */
    public StartFindingRepoEvent(Object source, long startRepoId) {
        super(source);
        this.startRepoId = startRepoId;
    }

    public long getStartRepoId() {
        return startRepoId;
    }
}
