package ru.vsu.plannerbackendv2.dao.persistence;

import ru.vsu.plannerbackendv2.dao.db.SubClassSQLMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExecutorImpl implements Executor {

    private SubClassSQLMapper mapper;

    public ExecutorImpl(SubClassSQLMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public Object execute(String queryTemplate, List<?> args, List<Condition> conditions, Connection connection) {
        var firstWord = queryTemplate.split(" ")[0];
        StringBuilder sb = new StringBuilder(queryTemplate);
        if (firstWord.equals("SELECT")) {
            conditions.forEach((it) -> sb.append(it.toSQL()));
        } else if (firstWord.equals("DELETE")) {
            if (conditions.size() > 0) {
                sb.append(" WHERE");
                conditions.forEach(it -> sb.append(it.toSQL()));
            }
        } else if (firstWord.equals("INSERT")) {
            sb.append(mapper.getInsertQueryPart(args.get(0)));
        } else if (firstWord.equals("UPDATE")) {
            sb.append(mapper.getUpdateQueryPart(args.get(0)));
            sb.append(" WHERE");
            conditions.forEach(it -> sb.append(it.toSQL()));
        }
        sb.append(";");
        if (!firstWord.equals("SELECT") && sb.toString().contains("AND")) {
            sb.replace(sb.indexOf("AND "), sb.indexOf("AND ") + 4, "");
        }
        var query = sb.toString();
        try {
            Statement statement = connection.createStatement();
            if (firstWord.equals("SELECT")) {
                ResultSet rs = statement.executeQuery(query);
                List<Object> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapper.map(rs));
                }
                return list;
            }
            statement.execute(query);
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Exception when executing query: " + query);
        }
    }
}
