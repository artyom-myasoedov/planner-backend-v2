package ru.vsu.plannerbackendv2.dao.db;

import org.reflections.Reflections;
import ru.vsu.plannerbackendv2.dao.db.annotation.Entity;
import ru.vsu.plannerbackendv2.dao.db.annotation.Table;
import ru.vsu.plannerbackendv2.dao.db.executor.FindByIdSQLExecutor;
import ru.vsu.plannerbackendv2.dao.db.executor.SQLQueryExecutor;
import ru.vsu.plannerbackendv2.dao.repository.JPARepository;
import ru.vsu.plannerbackendv2.di.annotation.Component;
import ru.vsu.plannerbackendv2.di.annotation.InjectByType;
import ru.vsu.plannerbackendv2.di.context.ApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JPAInvocationHandlerFactory {

    @InjectByType
    private ApplicationContext context;

    public JPAInvocationHandler create(Class<? extends JPARepository<?, ?>> classRepository) throws Exception {
        Map<String, String> methodNamesToSQLTemplates = new HashMap<>();
        Class modelClass = getModelClass(classRepository);
        String source = ((Table) modelClass.getAnnotation(Table.class)).schema() + "." + ((Table) modelClass.getAnnotation(Table.class)).name();
        Set<String> actions = Set.of("findBy, findAll");
        Set<Field> fields = Set.of(modelClass.getDeclaredFields());
        if (!modelClass.isAnnotationPresent(Entity.class)) {
            Reflections reflections = new Reflections("ru.vsu.dao");
            Set<Class<?>> subClasses = reflections.getSubTypesOf(modelClass);
        }
        Set<String> fieldNames = fields.stream().map(Field::getName).collect(Collectors.toSet());
        Set<String> conditions = Set.of("Like", "Contains", "GreaterThan", "LessThan", "And");
        var mapper = new SubClassSQLMapper(null, null, null, null, null);

        return new JPAInvocationHandler(methodNamesToSQLTemplates,
                new SQLQueryExecutor(context.getBean(ConnectionManager.class),
                        mapper),
                new FindByIdSQLExecutor(context.getBean(ConnectionManager.class),
                        mapper),
                context.getBean(SQLQueryPlaceHolder.class),
                modelClass);
    }

    private Class<?> getModelClass(Class<? extends JPARepository<?, ?>> classRepository) {
        ParameterizedType genericInterface = (ParameterizedType) classRepository.getGenericInterfaces()[0];
        return (Class<?>) genericInterface.getActualTypeArguments()[0];
    }
}
