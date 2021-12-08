package ru.vsu.plannerbackendv2.dao.db.sqlQueryCreator;

import ru.vsu.plannerbackendv2.di.annotation.Component;

import java.lang.reflect.Method;

@Component
public class AnnotationSQLQueryCreator implements SQLQueryCreator {
    @Override
    public String create(Method method, Object[] args) {
        return null;
    }
}
