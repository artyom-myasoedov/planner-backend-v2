package ru.vsu.plannerbackendv2.di.configuration;

import ru.vsu.plannerbackendv2.di.annotation.Component;
import ru.vsu.plannerbackendv2.di.parser.Parser;
import ru.vsu.plannerbackendv2.di.reflection.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaConfiguration implements Configuration {

    private final Reflections scanner;
    private final Map<Class<?>, Set<Class<?>>> ifc2ImplClasses;
    private final Map<String, Object> properties;
    private final Parser parser;

    public JavaConfiguration(String[] packagesToScan, Map<Class<?>, Set<Class<?>>> ifc2ImplClasses, Parser parser, String properties) {
        this.scanner = new Reflections(packagesToScan);
        this.parser = parser;
        this.properties = parser.parseMaps(properties);
        this.ifc2ImplClasses = ifc2ImplClasses;
    }

    @Override
    public <T> Set<Class<? extends T>> getImplClasses(Class<T> ifc) {
        return ifc2ImplClasses.computeIfAbsent(ifc, func -> scanner.getSubTypesAnnotatedBy(ifc, Component.class).stream()
                        .map(aClass -> (Class<?>) aClass)
                        .collect(Collectors.toSet()))
                .stream()
                .map(item -> (Class<? extends T>) item)
                .collect(Collectors.toSet());
    }

    @Override
    public Reflections getScanner() {
        return scanner;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }


}
