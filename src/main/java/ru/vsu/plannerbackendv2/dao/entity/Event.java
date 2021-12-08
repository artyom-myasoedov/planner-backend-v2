package ru.vsu.plannerbackendv2.dao.entity;


import ru.vsu.plannerbackendv2.dao.db.annotation.Column;
import ru.vsu.plannerbackendv2.dao.db.annotation.Id;
import ru.vsu.plannerbackendv2.dao.db.annotation.IdGeneratingStrategy;
import ru.vsu.plannerbackendv2.dao.db.annotation.Table;

import java.time.LocalDateTime;

@Table(schema = "planner", name = "events")
public abstract class Event {

    @Id(strategy = IdGeneratingStrategy.AUTO)
    @Column(name = "event_id")
    private Integer id;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "opponent_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "event_type_id")
    protected EventType eventType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventType getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", eventType=" + eventType +
                '}';
    }
}
