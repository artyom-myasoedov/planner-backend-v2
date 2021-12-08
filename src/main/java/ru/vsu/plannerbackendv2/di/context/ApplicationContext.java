package ru.vsu.plannerbackendv2.di.context;

import ru.vsu.plannerbackendv2.di.BeanFactory;
import ru.vsu.plannerbackendv2.di.annotation.Component;
import ru.vsu.plannerbackendv2.di.configuration.Configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ApplicationContext implements Context {
    private BeanFactory factory;
    private final Map<Class<?>, Object> cache = new ConcurrentHashMap<>();
    private final Configuration config;

    public ApplicationContext(Configuration config) {
        this.config = config;
        cache.put(ApplicationContext.class, this);
    }

    public <T> T getBean(Class<T> type) {
        Class<? extends T> implClass = type;

        if (type.isInterface()) {
            final Set<Class<? extends T>> implClasses = config.getImplClasses(type);
            if (implClasses.size() != 1)
                throw new RuntimeException("interface " + implClass.getName() + " has 0 or more than 1 implementations");

            implClass = implClasses.iterator().next();
        }
        if (cache.containsKey(implClass)) {
            return (T) cache.get(implClass);
        }

        T t = factory.createBean(implClass);

        cacheIfNeeded(t, implClass);

        return t;
    }

    @Override
    public <T> Collection<? extends T> getBeans(final Class<T> ifc) {
        if (ifc.isInterface()) {
            return config.getImplClasses(ifc).stream()
                    .map(aClass -> {
                        if (cache.containsKey(aClass)) {
                            return (T) cache.get(aClass);
                        } else {
                            T t = factory.createBean(aClass);
                            cacheIfNeeded(t, aClass);
                            return t;
                        }
                    })
                    .collect(Collectors.toList());
        }

        if (cache.containsKey(ifc)) {
            return Collections.singletonList((T) cache.get(ifc));
        }
        T bean = factory.createBean(ifc);
        cacheIfNeeded(bean, ifc);
        return Collections.singletonList(bean);
    }

    private <T> void cacheIfNeeded(T t, Class<? extends T> implClass) {
        if (implClass.isAnnotationPresent(Component.class)) {
            cache.put(implClass, t);
        }
    }

    public Configuration getConfig() {
        return config;
    }

    public Configuration getScanner() {
        return config;
    }

    public void setFactory(BeanFactory factory) {
        this.factory = factory;
    }

}
