package ru.gw3nax.kudagoapi.observer;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EntityEvent<T> extends ApplicationEvent {
    private final T entity;
    private final EntityEventType eventType;

    public EntityEvent(Object source, T entity, EntityEventType eventType) {
        super(source);
        this.eventType = eventType;
        this.entity = entity;
    }
}
