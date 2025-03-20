package com.stability.sequence.diagram.services.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.stability.config.MethodScanConfig;
import com.stability.sequence.diagram.resp.MethodFieldResp;
import com.stability.sequence.diagram.services.MethodService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class MethodServiceImpl implements MethodService {

    @Resource
    private MethodScanConfig methodScanConfig;

    @Override
    public List<MethodFieldResp> getAllMethod() {
        List<Class<?>> classList = methodScanConfig.getClassList();

        if (CollectionUtils.isEmpty(classList)) {
            return List.of();
        }

        return classList.stream().map(clazz -> Arrays.stream(clazz.getDeclaredMethods()).map(method -> {
            MethodFieldResp resp = new MethodFieldResp();
            resp.setClazzShortName(clazz.getName());
            resp.setShortMethod(method.getName());
            resp.setShortResp(method.getReturnType().getName());
            return resp;
        }).toList()).flatMap(Collection::stream).toList();
    }
}
