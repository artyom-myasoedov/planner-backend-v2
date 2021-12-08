package ru.vsu.plannerbackendv2.dao.persistence;

import ru.vsu.plannerbackendv2.dao.db.SubClassSQLMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExtractorImpl implements Extractor {


    private String source;

    private SubClassSQLMapper mapper;

    public ExtractorImpl(String source, SubClassSQLMapper mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    public Object extract(Connection connection, List<Condition> conditions) {
        StringBuilder stringBuilder = new StringBuilder("SELECT * FROM " + source);
        if (conditions.size() > 0) {
            stringBuilder.append(" WHERE");
            conditions.forEach(it -> stringBuilder.append(it.toSQL()));
        }
        stringBuilder.append(";");
        int i = stringBuilder.indexOf("AND ");
        if (i > -1)
            stringBuilder.replace(i, i + 4, "");
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(stringBuilder.toString());
            List<Object> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapper.map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Exception when extracting data from " + source, e);
        }
    }
}
