package ru.vsu.plannerbackendv2.dao.db;

import ru.vsu.plannerbackendv2.dao.db.annotation.Column;
import ru.vsu.plannerbackendv2.dao.entity.EventType;
import ru.vsu.plannerbackendv2.di.annotation.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SQLQueryPlaceHolder {

    public String putValues(String query, Object[] args, Class<?> returnType) throws IllegalAccessException {
        String operation = query.split(" ")[0];
        if (operation.equals("INSERT") || operation.equals("UPDATE")) {
            Object obj = args[0];
            Set<Field> declaredFields = new HashSet<>(Set.of(returnType.getDeclaredFields()));
            declaredFields.addAll(Set.of(obj.getClass().getDeclaredFields()));
            String[] columnNames = query.substring(query.indexOf('(') + 1, query.indexOf(')')).split(", ");
            Object[] vals = new Object[columnNames.length];
            for (int i = 0; i < columnNames.length; i++) {
                int finalI = i;
                var currField = declaredFields.stream()
                        .filter((it) -> it.getAnnotation(Column.class).name().equals(columnNames[finalI]))
                        .findFirst().orElseThrow(() -> new RuntimeException("No such field"));
                currField.setAccessible(true);
                vals[i] = currField.get(obj);
            }
            args = vals;
        }
        for (int i = 0; i < args.length; i++) {
            var val = args[i];
            var strVal = val.toString();
            if (val instanceof List) {
                int i1 = query.indexOf("inList(");
            }
            if (String.class.isAssignableFrom(val.getClass()) || LocalDateTime.class.isAssignableFrom(val.getClass())) {
                strVal = "`" + strVal + "`";
            }
            if (EventType.class.isAssignableFrom(val.getClass())) {
                strVal = Integer.toString(((EventType) val).getId() + 1);
            }
            query.replace("$" + i, strVal);
        }
        return query;

    }
}
