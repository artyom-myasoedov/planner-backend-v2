package ru.vsu.plannerbackendv2.service.impl;

import ru.vsu.plannerbackendv2.dao.entity.Event;
import ru.vsu.plannerbackendv2.dao.entity.EventType;
import ru.vsu.plannerbackendv2.dao.repository.EventRepository;
import ru.vsu.plannerbackendv2.di.annotation.Component;
import ru.vsu.plannerbackendv2.di.annotation.InjectByType;
import ru.vsu.plannerbackendv2.di.annotation.PostConstruct;
import ru.vsu.plannerbackendv2.service.EventService;
import ru.vsu.plannerbackendv2.service.TimeComparisonOperation;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Component
public class EventServiceImpl implements EventService {

    @InjectByType
    private EventRepository repository;

    private Map<TimeComparisonOperation, BiFunction<LocalDateTime, List<EventType>, Collection<Event>>> findByDateFunctions;

    @PostConstruct
    public void init() {
        findByDateFunctions = new HashMap<>() {{
            put(TimeComparisonOperation.EQUAL, (dateTime, eventTypes) -> repository.findByDateTimeAndEventTypeContains(dateTime, eventTypes));
            put(TimeComparisonOperation.AFTER, (dateTime, eventTypes) -> repository.findByDateTimeGreaterThanAndEventTypeContains(dateTime, eventTypes));
            put(TimeComparisonOperation.BEFORE, (dateTime, eventTypes) -> repository.findByDateTimeLessThanAndEventTypeContains(dateTime, eventTypes));
        }};
    }

    @Override
    public Event findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event with id: " + id + "doesn't exist"));
    }

    @Override
    public Event create(Event entity) {
        if (repository.findById(entity.getId()).isPresent())
            throw new RuntimeException("Event with id: " + entity.getId() + "has already existed");
        repository.add(entity);
        return entity;
    }

    @Override
    public Event update(Event entity) {
        if (repository.findById(entity.getId()).isEmpty())
            throw new RuntimeException("Event with id: " + entity.getId() + "hasn't existed");
        repository.update(entity);
        return entity;
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Collection<Event> findByTypes(List<EventType> types) {
        return repository.findByEventTypeContains(types);
    }

    @Override
    public Collection<Event> findAll() {
        return repository.findAll();
    }

    @Override
    public Collection<Event> findByYear(Integer year, List<EventType> types) {
        return repository.findByYearAndByEventTypeContains(year, types);
    }

    @Override
    public Collection<Event> findByMonth(Integer month, List<EventType> types) {
        return repository.findByMonthAndEventTypeContains(month, types);
    }

    @Override
    public Collection<Event> findByNameLike(String name, List<EventType> types) {
        return repository.findByNameLikeAndEventTypeContains(name, types);
    }

    @Override
    public Collection<Event> findByDate(Integer year, Integer month, Integer day, List<EventType> types, TimeComparisonOperation operation) {
        return findByDateFunctions.get(operation)
                .apply(LocalDateTime.of(year, month, day, 0, 0), types);
    }
}
