package ru.vsu.plannerbackendv2.di.postProcessor;

import ru.vsu.plannerbackendv2.di.annotation.Component;
import ru.vsu.plannerbackendv2.di.annotation.InjectByType;
import ru.vsu.plannerbackendv2.di.context.ApplicationContext;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

@Component
public class InjectByTypeAnnotationBeanPostProcessor implements BeanPostProcessor {
    @Override
    public void configure(Object t, ApplicationContext context) {
        for (Field field : t.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(InjectByType.class)) {
                field.setAccessible(true);
                Object object;
                if (Collection.class.isAssignableFrom(field.getType())) {
                    object = getCollection(context, field);
                } else {
                    object = context.getBean(field.getType());
                }
                try {
                    field.set(t, object);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("No access to the field " + t.getClass().getName() + "." + field.getName(), e);
                }
            }
        }
    }

    private Object getCollection(ApplicationContext context, Field field) {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        Type actualTypeArgument = type.getActualTypeArguments()[0];
        return context.getBeans((Class<?>) actualTypeArgument);
    }
}
