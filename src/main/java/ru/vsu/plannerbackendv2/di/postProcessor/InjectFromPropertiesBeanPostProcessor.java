package ru.vsu.plannerbackendv2.di.postProcessor;

import ru.vsu.plannerbackendv2.di.annotation.Component;
import ru.vsu.plannerbackendv2.di.annotation.InjectFromProperties;
import ru.vsu.plannerbackendv2.di.context.ApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InjectFromPropertiesBeanPostProcessor implements BeanPostProcessor {

    @Override
    public void configure(Object t, ApplicationContext context) {
        for (Field field : t.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(InjectFromProperties.class)) {
                field.setAccessible(true);
                if (Map.class.isAssignableFrom(field.getType())) {
                    injectMap(t, context, field);
                } else {
                    Object content = context.getConfig().getProperties().get(field.getName());
                    try {
                        field.set(t, content);
                    } catch (Exception e) {
                        throw new RuntimeException("Exception when injecting from properties field " + t.getClass().getName() + "." + field.getName());
                    }
                }
            }
        }
    }

    private void injectMap(Object t, ApplicationContext context, Field field) {
        try {
            Map<String, ?> content = (Map<String, ?>) ((List<?>) context.getConfig().getProperties().get("maps"))
                    .stream().filter(item -> ((Map<String, ?>) item).get("name").equals(field.getName()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("There is no map property " + field.getName()));
            Class<?> keyClass = Class.forName((String) content.get("key_type"));
            List<?> entries = (List<?>) content.get("entries");

            if (Enum.class.isAssignableFrom(keyClass)) {
                Method valueOf = keyClass.getMethod("valueOf", String.class);
                Map<Object, Object> res = new HashMap<>();
                entries.forEach(item -> {
                    try {
                        res.put(valueOf.invoke(null, ((Map<?, ?>) item).get("key").toString()),
                                context.getBean(Class.forName((String) ((Map<?, ?>) item).get("value"))));
                    } catch (Exception e) {
                        throw new RuntimeException("Invalid data in configurations");
                    }
                });

                field.set(t, res);
            } else {
                throw new UnsupportedOperationException("Cannot inject map with key type" + keyClass.getName() + " unsupported operation");
            }
        } catch (Throwable e) {
            throw new RuntimeException("Exception when injecting map " + t.getClass().getName() + "." + field.getName(), e);
        }
    }
}
