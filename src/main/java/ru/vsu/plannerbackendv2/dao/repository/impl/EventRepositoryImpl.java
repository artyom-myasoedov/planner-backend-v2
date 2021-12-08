package ru.vsu.plannerbackendv2.dao.repository.impl;

import ru.vsu.plannerbackendv2.dao.db.ConnectionManager;
import ru.vsu.plannerbackendv2.dao.db.SubClassSQLMapper;
import ru.vsu.plannerbackendv2.dao.db.annotation.Column;
import ru.vsu.plannerbackendv2.dao.entity.Birthday;
import ru.vsu.plannerbackendv2.dao.entity.Event;
import ru.vsu.plannerbackendv2.dao.entity.EventType;
import ru.vsu.plannerbackendv2.dao.entity.Meeting;
import ru.vsu.plannerbackendv2.dao.persistence.*;
import ru.vsu.plannerbackendv2.dao.repository.EventRepository;
import ru.vsu.plannerbackendv2.di.annotation.Component;
import ru.vsu.plannerbackendv2.di.annotation.InjectByType;
import ru.vsu.plannerbackendv2.di.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class EventRepositoryImpl implements EventRepository {

    @InjectByType
    private ConnectionManager connectionManager;

    private Extractor extractor;

    private Executor executor;

    public EventRepositoryImpl() {
    }


    @PostConstruct
    public void init() {
        var mapper = new SubClassSQLMapper("event_type_id",
                Event.class,
                Map.of(0, Birthday.class, 1, Meeting.class),
                getColumnNamesToFields(Event.class),
                Map.of(Birthday.class, getColumnNamesToFields(Birthday.class), Meeting.class, getColumnNamesToFields(Meeting.class))
        );
        extractor = new ExtractorImpl("planner.events", mapper);
        executor = new ExecutorImpl(mapper);
    }


    @Override
    public Collection<Event> findByEventTypeContains(List<EventType> types) {
        return (Collection<Event>) extractor.extract(connectionManager.getConnection(),
                List.of(new Condition("event_type_id", types, "ContainsIn")));
    }

    @Override
    public Collection<Event> findByYearAndByEventTypeContains(Integer year, List<EventType> types) {
        return (Collection<Event>) executor.execute("SELECT * FROM planner.events WHERE date_part('year', date_time) = " + year,
                Collections.emptyList(), List.of(new Condition("event_type_id", types, "ContainsIn")), connectionManager.getConnection());
    }

    @Override
    public Collection<Event> findByMonthAndEventTypeContains(Integer month, List<EventType> types) {
        return (Collection<Event>) executor.execute("SELECT * FROM planner.events WHERE date_part('month', date_time) = " + month,
                Collections.emptyList(), List.of(new Condition("event_type_id", types, "ContainsIn")), connectionManager.getConnection());

    }

    @Override
    public Collection<Event> findByNameLikeAndEventTypeContains(String name, List<EventType> types) {
        return (Collection<Event>) extractor.extract(connectionManager.getConnection(),
                List.of(new Condition("opponent_name", name, "Like"),
                        new Condition("event_type_id", types, "ContainsIn")));
    }

    @Override
    public Collection<Event> findByDateTimeAndEventTypeContains(LocalDateTime dateTime, List<EventType> types) {
        return (Collection<Event>) extractor.extract(connectionManager.getConnection(), List.of(
                new Condition("date_time", dateTime, "Equal"),
                new Condition("event_type_id", types, "ContainsIn")));
    }

    @Override
    public Collection<Event> findByDateTimeGreaterThanAndEventTypeContains(LocalDateTime dateTime, List<EventType> types) {
        return (Collection<Event>) extractor.extract(connectionManager.getConnection(), List.of(
                new Condition("date_time", dateTime, "GreaterThan"),
                new Condition("event_type_id", types, "ContainsIn")));
    }

    @Override
    public Collection<Event> findByDateTimeLessThanAndEventTypeContains(LocalDateTime dateTime, List<EventType> types) {
        return (Collection<Event>) extractor.extract(connectionManager.getConnection(), List.of(
                new Condition("date_time", dateTime, "LessThan"),
                new Condition("event_type_id", types, "ContainsIn")));
    }

    @Override
    public Optional<Event> findById(Integer id) {
        List<Event> retVal = (List<Event>) extractor.extract(connectionManager.getConnection(),
                List.of(new Condition("event_id", id, "Equal")));
        if (retVal.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(retVal.get(0));
    }

    @Override
    public void add(Event entity) {
        executor.execute("INSERT INTO planner.events  ", List.of(entity),
                Collections.emptyList(),
                connectionManager.getConnection());
    }

    @Override
    public void update(Event entity) {
        executor.execute("UPDATE planner.events SET ", List.of(entity),
                List.of(new Condition("event_id", entity.getId(), "Equal")),
                connectionManager.getConnection());
    }

    @Override
    public void deleteById(Integer id) {
        executor.execute("DELETE FROM planner.events ", Collections.emptyList(),
                List.of(new Condition("event_id", id, "Equal")),
                connectionManager.getConnection());

    }

    @Override
    public Collection<Event> findAll() {
        return (Collection<Event>) extractor.extract(connectionManager.getConnection(), Collections.emptyList());
    }


    private Map<String, String> getColumnNamesToFields(Class<?> modelClass) {
        Map<String, String> retVal = new HashMap<>();
        Arrays.stream(modelClass.getDeclaredFields()).forEach(
                (it) -> retVal.put(it.getAnnotation(Column.class).name(), it.getName()));
        return retVal;
    }
}
