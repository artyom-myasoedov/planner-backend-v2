package ru.vsu.plannerbackendv2.dao.repository;

import java.util.Collection;
import java.util.Optional;


public interface JPARepository<T, K> {

    Optional<T> findById(K id);

    void add(T entity);

    void update(T entity);

    void deleteById(K id);

    Collection<T> findAll();


}
