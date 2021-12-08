package ru.vsu.plannerbackendv2.dao.db.sqlQueryCreator;

import java.lang.reflect.Method;

@FunctionalInterface
public interface SQLQueryCreator {

    String create(Method method, Object[] args);
}
