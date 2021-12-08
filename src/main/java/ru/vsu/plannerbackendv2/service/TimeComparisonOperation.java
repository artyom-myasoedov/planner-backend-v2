package ru.vsu.plannerbackendv2.service;

import java.util.Optional;

public enum TimeComparisonOperation {
    EQUAL(0),
    BEFORE(1),
    AFTER(2);


    private final Integer id;

    TimeComparisonOperation(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public static Optional<TimeComparisonOperation> fromInteger(Integer id) {
        if (id == null) {
            return Optional.empty();
        }

        for (var value : TimeComparisonOperation.values()) {
            if (value.id.equals(id)) {
                return Optional.of(value);
            }
        }

        return Optional.empty();
    }
}
