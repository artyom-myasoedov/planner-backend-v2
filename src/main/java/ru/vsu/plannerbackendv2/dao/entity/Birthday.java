package ru.vsu.plannerbackendv2.dao.entity;

import ru.vsu.plannerbackendv2.dao.db.annotation.Column;
import ru.vsu.plannerbackendv2.dao.db.annotation.Entity;

@Entity
public class Birthday  extends Event{

    @Column(name = "present")
    private String present;

    public Birthday() {
        eventType = EventType.BIRTHDAY;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    @Override
    public String toString() {
        return "Birthday {\n" +
                "id = " + getId() +
                ",\n dateTime = " + getDateTime() +
                ",\n name = '" + getName() + '\'' +
                ",\n description = '" + getDescription() + "'," +
                "\n present = " + getPresent() +
                "\n}";
    }
}
