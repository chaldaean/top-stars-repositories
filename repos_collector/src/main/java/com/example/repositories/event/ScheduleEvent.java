package com.example.repositories.event;

import org.springframework.context.ApplicationEvent;

public class ScheduleEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ScheduleEvent(Object source) {
        super(source);
    }
}
