package ru.vsu.plannerbackendv2.dao.entity;

import ru.vsu.plannerbackendv2.dao.db.annotation.Entity;

@Entity
public class Meeting extends Event {

    public Meeting() {
        eventType = EventType.MEETING;
    }

    @Override
    public String toString() {
        return "Meeting {\n" +
                "id = " + getId() +
                ",\n dateTime = " + getDateTime() +
                ",\n name = '" + getName() + '\'' +
                ",\n description = '" + getDescription() + "'" +
                "\n}";
    }
}
