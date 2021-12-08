package ru.vsu.plannerbackendv2.dao.persistence;

import java.sql.Connection;
import java.util.List;


@FunctionalInterface
public interface Extractor {

    Object extract(Connection connection, List<Condition> conditions);
}
