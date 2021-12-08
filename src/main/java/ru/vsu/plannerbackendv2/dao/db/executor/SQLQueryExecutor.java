package ru.vsu.plannerbackendv2.dao.db.executor;

import ru.vsu.plannerbackendv2.dao.db.ConnectionManager;
import ru.vsu.plannerbackendv2.dao.db.SubClassSQLMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SQLQueryExecutor implements QueryExecutor {

    private ConnectionManager connectionManager;

    private SubClassSQLMapper mapper;

    private List<String> commonKeyWords;

    public SQLQueryExecutor(ConnectionManager connectionManager, SubClassSQLMapper mapper) {
        this.connectionManager = connectionManager;
        this.mapper = mapper;
        commonKeyWords = List.of("UPDATE", "INSERT", "DELETE");
    }

    @Override
    public Object execute(String query, Class<?> returnType) {
        try {
            Connection connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet;
            if (!Collection.class.isAssignableFrom(returnType)) {
                var keyWord = query.split(" ")[0];
                if (commonKeyWords.contains(keyWord)) {
                    resultSet = statement.executeQuery(query);
                    return !resultSet.next();
                }
                throw new RuntimeException("Unsupported operation: " + query);
            }
            Collection<Object> retVal = new ArrayList<>();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                retVal.add(mapper.map(resultSet));
            }
            return retVal;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
