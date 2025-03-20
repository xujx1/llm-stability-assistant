package com.stability.config;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.stability.annotations.AnnotatedScanner;
import com.stability.annotations.Tool;
import com.stability.constant.Constant;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "toolScanConfig")
public class ToolScanConfig implements InitializingBean {

    @Resource
    private AnnotatedScanner annotatedScanner;

    @Getter
    private final List<String> methodNames = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<Class<?>> classesWithToolAnnotation = annotatedScanner.findClassesAnnotated(Tool.class,
            Constant.TOOL_PACKAGE);
        for (Class<?> clazz : classesWithToolAnnotation) {
            Method[] clazzMethods = clazz.getDeclaredMethods();

            methodNames.addAll(Arrays.stream(clazzMethods).
                filter(method -> Modifier.isPublic(method.getModifiers())).
                map(Method::getName).toList());
        }
    }
}
