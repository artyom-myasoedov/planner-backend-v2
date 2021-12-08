package ru.vsu.plannerbackendv2.di.reflection;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Reflections {

    private final String[] basePackages;
    private final ClassScanner scanner;
    private final Set<Class<?>> availableClasses;


    public Reflections(String[] basePackages) {
        this.basePackages = basePackages;
        scanner = new ClassScanner();
        availableClasses = Arrays.stream(this.basePackages)
                .flatMap(dir -> scanner.findClasses(dir).stream())
                .collect(Collectors.toSet());
    }

    public String[] getBasePackages() {
        return basePackages;
    }

    public ClassScanner getScanner() {
        return scanner;
    }

    public Set<Class<?>> getAvailableClasses() {
        return availableClasses;
    }

    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> root) {
        return availableClasses.stream()
                .filter(root::isAssignableFrom)
                .filter(aClass -> !aClass.isInterface())
                .map(aClass -> (Class<? extends T>) aClass)
                .collect(Collectors.toSet());
    }

    public <T> Set<Class<? extends T>> getSubTypesAnnotatedBy(Class<T> root, Class<? extends Annotation> annotation) {
        return getSubTypesOf(root).stream()
                .filter(aClass -> aClass.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }
}
