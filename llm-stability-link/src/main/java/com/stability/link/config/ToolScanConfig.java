package com.stability.link.config;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.stability.link.annotations.AnnotatedScanner;
import com.stability.link.annotations.Tool;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

@Configuration(value = "toolScanConfig")
public class ToolScanConfig implements InitializingBean {

    private static final String TOOL_PACKAGE = "com.stability.link.tools";

    @Resource
    private AnnotatedScanner annotatedScanner;

    @Getter
    private final List<String> methodNames = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<Class<?>> classesWithToolAnnotation = annotatedScanner.findClassesAnnotated(Tool.class, TOOL_PACKAGE);
        for (Class<?> clazz : classesWithToolAnnotation) {
            Method[] clazzMethods = clazz.getDeclaredMethods();

            methodNames.addAll(Arrays.stream(clazzMethods).
                filter(method -> Modifier.isPublic(method.getModifiers())).
                map(Method::getName).toList());
        }
    }
}
