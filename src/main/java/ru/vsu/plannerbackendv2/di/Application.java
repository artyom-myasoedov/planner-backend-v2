package ru.vsu.plannerbackendv2.di;

import ru.vsu.plannerbackendv2.di.configuration.JavaConfiguration;
import ru.vsu.plannerbackendv2.di.context.ApplicationContext;
import ru.vsu.plannerbackendv2.di.parser.impl.YAMLParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Application {
    public static ApplicationContext context;
    private static final YAMLParser parser = new YAMLParser();

    public static ApplicationContext run(String[] packagesToScan, Map<Class<?>, Set<Class<?>>> ifc2ImplClass, String properties) {
        JavaConfiguration config = new JavaConfiguration(packagesToScan, ifc2ImplClass, new YAMLParser(), properties);
        ApplicationContext context = new ApplicationContext(config);
        BeanFactory objectFactory = new BeanFactory(context);
        context.setFactory(objectFactory);
        return context;
    }

    public static ApplicationContext getContext() {
        if (context == null) {
            context = run(new String[]{parser.parseMaps("application.yaml").get("basePackage").toString()},
                    new HashMap<>(){{put(Map.class, Set.of(HashMap.class));}}, "application.yaml");
        }
        return context;
    }

}
