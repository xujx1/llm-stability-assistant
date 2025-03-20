package com.stability.sequence.diagram.tools;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

import com.stability.sequence.diagram.stat.JCallGraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import com.stability.annotations.Tool;

@Tool
@Slf4j
public class MethodTool {

    public record MethodCallRequest(String className, String methodName) {}

    @Bean
    @Description("根据类名+方法名判断方法是否存在")
    public Function<MethodCallRequest, Boolean> methodExist() {
        return (request) -> {
            try {
                Class<?> clazz = Class.forName(request.className);
                clazz.getMethod(request.methodName);
                return true;
            } catch (Exception e) {
                return false;
            }
        };
    }

    @Bean
    @Description("根据类名+方法名解析方法的路径")
    public Function<MethodCallRequest, List<String>> methodCall() {
        return (request) -> {
            try {
                Class<?> clazz = Class.forName(request.className);
                Method method = clazz.getMethod(request.methodName);
                return JCallGraph.methodCalls(clazz, method);
            } catch (Exception e) {
                log.error("methodCall error", e);
                return List.of();
            }
        };
    }
}
