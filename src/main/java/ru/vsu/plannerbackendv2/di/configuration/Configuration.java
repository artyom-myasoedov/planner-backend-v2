package ru.vsu.plannerbackendv2.di.configuration;

import ru.vsu.plannerbackendv2.di.reflection.Reflections;

import java.util.Map;
import java.util.Set;

public interface Configuration {

    <T> Set<Class<? extends T>> getImplClasses(Class<T> ifc);

    Reflections getScanner();

    Map<String, Object> getProperties();

}
