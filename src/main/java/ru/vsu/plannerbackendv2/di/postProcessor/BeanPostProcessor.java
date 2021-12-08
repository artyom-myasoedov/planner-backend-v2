package ru.vsu.plannerbackendv2.di.postProcessor;

import ru.vsu.plannerbackendv2.di.context.ApplicationContext;

public interface BeanPostProcessor {
    void configure(Object t, ApplicationContext context);

}
