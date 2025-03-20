package com.stability.link.annotations;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

@Configuration
public class AnnotatedScanner {

    public Set<Class<?>> findClassesAnnotated(Class<? extends Annotation> annotationClass, String packageName) {
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        // 扫描指定包下的bean
        scanner.scan(packageName);
        // 获取bean
        Map<String, Object> objectMap = registry.getBeansWithAnnotation(annotationClass);

        if (MapUtils.isEmpty(objectMap)) {
            return Set.of();
        }

        return objectMap.values().stream().map(Object::getClass).collect(Collectors.toSet());
    }
}
