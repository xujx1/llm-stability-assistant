package com.stability.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.stability.constant.Constant;
import com.stability.annotations.AnnotatedScanner;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@Configuration(value = "methodScanConfig")
public class MethodScanConfig implements InitializingBean {

    @Resource
    private AnnotatedScanner annotatedScanner;

    @Getter
    private final List<Class<?>> classList = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<Class<?>> classesWithToolAnnotation =
            annotatedScanner.findClassesAnnotated(Controller.class, Constant.TOOL_PACKAGE);

        for (Class<?> clazz : classesWithToolAnnotation) {
            classList.add(clazz);
        }
    }
}
