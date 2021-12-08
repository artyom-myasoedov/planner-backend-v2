package ru.vsu.plannerbackendv2.dao.db.executor;

import ru.vsu.plannerbackendv2.dao.db.ConnectionManager;
import ru.vsu.plannerbackendv2.dao.db.SubClassSQLMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class FindByIdSQLExecutor implements QueryExecutor {

    private ConnectionManager connectionManager;

    private SubClassSQLMapper mapper;

    public FindByIdSQLExecutor(ConnectionManager connectionManager, SubClassSQLMapper mapper) {
        this.connectionManager = connectionManager;
        this.mapper = mapper;
    }

    @Override
    public Object execute(String query, Class<?> returnType) {
        Connection connection = connectionManager.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return Optional.ofNullable(mapper.map(resultSet));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
