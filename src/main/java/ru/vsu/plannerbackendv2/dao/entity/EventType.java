package ru.vsu.plannerbackendv2.dao.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum EventType {
    BIRTHDAY(0),
    MEETING(1),
    ANY(2);

    private final Integer id;

    EventType(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static Optional<EventType> fromInteger(Integer id) {
        if (id == null) {
            return Optional.empty();
        }

        for (var value : EventType.values()) {
            if (value.id.equals(id)) {
                return Optional.of(value);
            }
        }

        return Optional.empty();
    }

    public static List<EventType> toList(EventType eventType) {
        if (!eventType.equals(EventType.ANY)) {
            return Collections.singletonList(eventType);
        }
        return Arrays.stream(EventType.values())
                .filter(eventType1 -> !eventType1.equals(EventType.ANY))
                .collect(Collectors.toList());
    }
}
