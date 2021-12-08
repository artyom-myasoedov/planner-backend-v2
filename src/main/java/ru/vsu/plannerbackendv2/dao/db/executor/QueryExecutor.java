package ru.vsu.plannerbackendv2.dao.db.executor;

@FunctionalInterface
public interface QueryExecutor {

    Object execute(String query, Class<?> returnType);
}
