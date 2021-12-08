package ru.vsu.plannerbackendv2.di;

import ru.vsu.plannerbackendv2.di.annotation.Component;
import ru.vsu.plannerbackendv2.di.annotation.PostConstruct;
import ru.vsu.plannerbackendv2.di.context.ApplicationContext;
import ru.vsu.plannerbackendv2.di.postProcessor.BeanPostProcessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class BeanFactory {
    private final ApplicationContext context;
    private final List<BeanPostProcessor> postProcessors;

    public BeanFactory(ApplicationContext context) {
        this.context = context;
        postProcessors = context.getConfig().getScanner().getSubTypesAnnotatedBy(BeanPostProcessor.class, Component.class)
                .stream()
                .map(aClass -> {
                    try {
                        return aClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Exception when creating configurator " + aClass.getName(), e);
                    }
                }).collect(Collectors.toList());
    }

    public <T> T createBean(Class<T> implClass) {

        T t;
        try {
            t = create(implClass);
        } catch (Exception e) {
            throw new RuntimeException("Exception when creating bean " + implClass.getName(), e);
        }

        configure(t);

        invokeInit(implClass, t);

        return t;

    }

    private <T> void invokeInit(Class<T> implClass, T t) {
        boolean postConstructInvoked = false;
        for (Method method : implClass.getMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                try {
                    if (postConstructInvoked)
                        throw new RuntimeException("Bean " + implClass.getName() + " has more than 1 @PostConstruct methods");
                    method.invoke(t);
                    postConstructInvoked = true;
                } catch (Exception e) {
                    throw new RuntimeException("exception in @PostConstruct method "
                            + implClass.getName() + "." + method.getName(), e);
                }
            }
        }
    }

    private <T> void configure(T t) {
        postProcessors.forEach(objectConfigurator -> objectConfigurator.configure(t, context));
    }

    private <T> T create(Class<T> implClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return implClass.getDeclaredConstructor().newInstance();
    }
}
