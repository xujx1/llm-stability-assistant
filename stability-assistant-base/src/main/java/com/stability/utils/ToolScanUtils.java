package com.stability.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.stability.annotations.AnnotatedScanner;
import com.stability.annotations.Tool;
import com.stability.constant.Constant;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.stereotype.Component;

@Component
public class ToolScanUtils {

    @Resource
    private AnnotatedScanner annotatedScanner;

    public List<String> findTool(String packageName) {
        Set<Class<?>> classesWithToolAnnotation = annotatedScanner.findClassesAnnotated(Tool.class, packageName);

        List<String> methodNames = new ArrayList<>();

        if (CollectionUtils.isEmpty(classesWithToolAnnotation)) {
            return methodNames;
        }

        for (Class<?> clazz : classesWithToolAnnotation) {
            Method[] clazzMethods = clazz.getDeclaredMethods();

            methodNames.addAll(Arrays.stream(clazzMethods).
                filter(method -> Modifier.isPublic(method.getModifiers())).
                map(Method::getName).toList());
        }

        return methodNames;
    }
}
