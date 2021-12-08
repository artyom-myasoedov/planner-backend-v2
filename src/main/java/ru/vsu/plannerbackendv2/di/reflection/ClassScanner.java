package ru.vsu.plannerbackendv2.di.reflection;

import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ClassScanner {

    private static final char PKG_SEPARATOR = '.';

    private static final char DIR_SEPARATOR = '/';

    private static final String CLASS_FILE_SUFFIX = ".class";

    public Set<Class<?>> findClasses(String scannedPackage) {
        String scannedPath = String.valueOf(ClassLoader
                        .getSystemClassLoader()
                        .getResource(scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR)))
                .replaceAll("libs[\\w\\W]*!", "classes/java/main")
                .replaceAll("jar:", "");
        File[] files;
        try {
            files = new File(URI.create(scannedPath)).listFiles();
        } catch (Exception e) {
            throw new RuntimeException("Exception when converting URL to URI", e);
        }
        Set<Class<?>> classes = new HashSet<>();
        for (File file : Objects.requireNonNull(files)) {
            classes.addAll(findClasses(file, scannedPackage));
        }
        return classes;
    }

    private Set<Class<?>> findClasses(File file, String scannedPackage) {
        Set<Class<?>> classes = new HashSet<>();
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            for (File child : Objects.requireNonNull(file.listFiles())) {
                classes.addAll(findClasses(child, resource));
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Cannot get class from name " + className, e);
            }
        }
        return classes;
    }
}
