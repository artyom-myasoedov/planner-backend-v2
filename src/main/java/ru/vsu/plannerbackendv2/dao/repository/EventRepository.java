package ru.vsu.plannerbackendv2.dao.repository;

import ru.vsu.plannerbackendv2.dao.db.annotation.Query;
import ru.vsu.plannerbackendv2.dao.db.annotation.Repository;
import ru.vsu.plannerbackendv2.dao.entity.Event;
import ru.vsu.plannerbackendv2.dao.entity.EventType;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface EventRepository extends JPARepository<Event, Integer> {

    Collection<Event> findByEventTypeContains(List<EventType> types);

    @Query(query = "")
    Collection<Event> findByYearAndByEventTypeContains(Integer year, List<EventType> types);

    @Query(query = "")
    Collection<Event> findByMonthAndEventTypeContains(Integer month, List<EventType> types);

    Collection<Event> findByNameLikeAndEventTypeContains(String name, List<EventType> types);

    Collection<Event> findByDateTimeAndEventTypeContains(LocalDateTime dateTime, List<EventType> types);

    Collection<Event> findByDateTimeGreaterThanAndEventTypeContains(LocalDateTime dateTime, List<EventType> types);

    Collection<Event> findByDateTimeLessThanAndEventTypeContains(LocalDateTime dateTime, List<EventType> types);

}
