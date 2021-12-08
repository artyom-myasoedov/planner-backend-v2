package ru.vsu.plannerbackendv2.dao.persistence;

import ru.vsu.plannerbackendv2.dao.entity.EventType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Condition {

    private String columnName;

    private Object fieldValue;

    private String condition;

    private static Map<String, String> condToChar = new HashMap<>() {{
        put("Equal", "=");
        put("LessThan", "<");
        put("GreaterThan", ">");
    }};

    public Condition(String columnName, Object fieldValue, String condition) {
        this.columnName = columnName;
        this.fieldValue = fieldValue;
        this.condition = condition;
    }

    public String getColumnName() {
        return columnName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public String getCondition() {
        return condition;
    }

    public String toSQL() {
        StringBuilder sb = new StringBuilder();
        if (condition.equals("ContainsIn")) {
            return fromContainsIn(sb);
        }
        if (condition.equals("Like")) {
            return fromLike(sb);
        }
        sb.append(" AND ")
                .append(columnName)
                .append(" ")
                .append(condToChar.get(condition))
                .append(" ");
        if (fieldValue instanceof String || fieldValue instanceof LocalDateTime) {
            if (fieldValue instanceof LocalDateTime) {
                fieldValue = Timestamp.valueOf((LocalDateTime) fieldValue);
                sb.append("timestamp ");
            }
            sb.append("'")
                    .append(fieldValue)
                    .append("'");
        } else sb.append(fieldValue);
        return sb.toString();
    }

    private String fromLike(StringBuilder sb) {
        return sb.append(" AND ")
                .append(columnName)
                .append(" LIKE '%")
                .append(fieldValue.toString())
                .append("%'").toString();
    }

    private String fromContainsIn(StringBuilder sb) {
        try {
            List<EventType> list = (List<EventType>) fieldValue;
            list.forEach(it -> {
                sb.append("OR ")
                        .append(columnName)
                        .append(" = ")
                        .append(it.getId());
            });
            sb.append(")");
            sb.replace(0, 3, " AND (");
            if (list.size() == 1) {
                sb.replace(sb.indexOf("("), sb.indexOf("(") + 1, "");
                sb.replace(sb.indexOf(")"), sb.indexOf(")") + 1, "");
            }
            return sb.toString();
        } catch (ClassCastException e) {
            throw new RuntimeException("Invalid type, exception when class casting", e);
        }
    }
}
