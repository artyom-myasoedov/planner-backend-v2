package ru.vsu.plannerbackendv2.dao.db;

import ru.vsu.plannerbackendv2.dao.db.annotation.Column;
import ru.vsu.plannerbackendv2.dao.db.annotation.Id;
import ru.vsu.plannerbackendv2.dao.entity.EventType;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class SubClassSQLMapper {

    private String predicateColumn;

    private Class<?> modelClass;

    private Map<?, Class<?>> classesViewToClasses;

    private Map<String, String> columnsToGeneralFields;

    private Map<Class<?>, Map<String, String>> subClassesColumnsToFields;

    public SubClassSQLMapper(String predicateColumn, Class<?> modelClass, Map<?, Class<?>> classesViewToClasses, Map<String, String> columnsToGeneralFields, Map<Class<?>, Map<String, String>> subClassesColumnsToFields) {
        this.predicateColumn = predicateColumn;
        this.modelClass = modelClass;
        this.classesViewToClasses = classesViewToClasses;
        this.columnsToGeneralFields = columnsToGeneralFields;
        this.subClassesColumnsToFields = subClassesColumnsToFields;
    }

    public Object map(ResultSet rs) {
        try {
            var classView = rs.getObject(predicateColumn);
            Class<?> classToMapping = classesViewToClasses.getOrDefault(classView, modelClass);
            Object retVal = classToMapping.getDeclaredConstructor().newInstance();
            setFields(rs, modelClass, retVal, columnsToGeneralFields);
            if (!classToMapping.equals(modelClass)) {
                setFields(rs, classToMapping, retVal, subClassesColumnsToFields.get(classToMapping));
            }
            return retVal;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getInsertQueryPart(Object obj) {
        List<Field> fields = getAllFields(obj);
        fields.sort(Comparator.comparing(Field::getName));
        StringBuilder sb = new StringBuilder("(");
        fields.forEach(it -> {
            sb.append(it.getAnnotation(Column.class).name())
                    .append(", ");
        });
        sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1, ")");
        sb.append(" VALUES (");
        fields.forEach(it -> {
            try {
                it.setAccessible(true);
                var val = it.get(obj);
                if (it.isAnnotationPresent(Id.class)) {
                    sb.append("DEFAULT");
                } else {
                    if (val instanceof String) {
                        sb.append("'")
                                .append(val)
                                .append("'");
                    } else if (val instanceof LocalDateTime) {
                        sb.append("'")
                                .append(Timestamp.valueOf((LocalDateTime) val))
                                .append("'");
                    } else if (val instanceof EventType) {
                        sb.append(((EventType) val).getId());
                    } else {
                        sb.append(val);
                    }
                }
                sb.append(", ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",") + 2, ")");
        return sb.toString();
    }

    private List<Field> getAllFields(Object obj) {
        Class<?> currClass = obj.getClass();
        Field[] genFields = modelClass.getDeclaredFields();
        List<Field> fields = new ArrayList<>(Arrays.asList(genFields));
        fields.addAll(Arrays.asList(currClass.getDeclaredFields()));
        return fields;
    }

    public String getUpdateQueryPart(Object obj) {
        List<Field> fields = getAllFields(obj);
        fields.removeIf(it -> it.isAnnotationPresent(Id.class));
        fields.sort(Comparator.comparing(Field::getName));
        StringBuilder sb = new StringBuilder();
        fields.forEach(it -> {
            try {
                it.setAccessible(true);
                var val = it.get(obj);
                sb.append(it.getAnnotation(Column.class).name())
                        .append(" = ");
                if (val instanceof String) {
                    sb.append("'")
                            .append(val)
                            .append("'");
                } else if (val instanceof LocalDateTime) {
                    sb.append("'")
                            .append(Timestamp.valueOf((LocalDateTime) val))
                            .append("'");
                } else if (val instanceof EventType) {
                    sb.append(((EventType) val).getId());
                } else {
                    sb.append(val);
                }
                sb.append(", ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1, "");
        return sb.toString();
    }

    private void setFields(ResultSet rs, Class<?> classToMapping, Object retVal, Map<String, String> columnsToFields) throws NoSuchFieldException, SQLException, IllegalAccessException {
        for (String key : columnsToFields.keySet()) {
            Field declaredField = classToMapping.getDeclaredField(columnsToFields.get(key));
            declaredField.setAccessible(true);
            var val = rs.getObject(key);
            if (LocalDateTime.class.isAssignableFrom(declaredField.getType())) {
                val = ((Timestamp) val).toLocalDateTime();
            }
            if (EventType.class.isAssignableFrom(declaredField.getType())) {
                val = EventType.fromInteger((Integer) val).orElseThrow(() -> new RuntimeException("keyColumn: " + key + " field: " + columnsToFields.get(key)));
            }
            declaredField.set(retVal, val);
        }
    }
}
