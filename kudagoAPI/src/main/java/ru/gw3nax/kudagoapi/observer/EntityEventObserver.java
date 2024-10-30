package ru.gw3nax.kudagoapi.observer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EntityEventObserver {

    @EventListener
    public <T> void handleEntityEvent(EntityEvent<T> event) {
        T entity = event.getEntity();
        EntityEventType entityEventType = event.getEventType();

        switch (entityEventType) {
            case CREATED -> log.info("Entity created: " + entity);
            case UPDATED -> log.info("Entity updated: " + entity);
            case DELETED -> log.info("Entity deleted: " + entity);
        }
    }
}
