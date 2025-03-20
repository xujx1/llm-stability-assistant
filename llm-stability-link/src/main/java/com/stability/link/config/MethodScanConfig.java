package com.stability.link.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.stability.link.annotations.AnnotatedScanner;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@Configuration(value = "methodScanConfig")
public class MethodScanConfig implements InitializingBean {

    @Resource
    private AnnotatedScanner annotatedScanner;

    private static final String TOOL_PACKAGE = "com.stability.link.client";

    @Getter
    private final List<Class<?>> classList = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<Class<?>> classesWithToolAnnotation =
            annotatedScanner.findClassesAnnotated(Controller.class, TOOL_PACKAGE);

        for (Class<?> clazz : classesWithToolAnnotation) {
            classList.add(clazz);
        }
    }
}
