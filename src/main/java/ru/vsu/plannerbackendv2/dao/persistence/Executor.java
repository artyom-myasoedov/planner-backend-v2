package ru.vsu.plannerbackendv2.dao.persistence;


import java.sql.Connection;
import java.util.List;

public interface Executor {


    Object execute(String queryTemplate, List<?> args, List<Condition> conditions, Connection connection);
}
