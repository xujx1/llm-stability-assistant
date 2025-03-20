package com.stability.sequence.diagram.config;

import java.util.ArrayList;
import java.util.List;

import com.stability.sequence.diagram.constant.DiagramConstant;
import com.stability.utils.ToolScanUtils;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "sequenceDiagramToolScanConfig")
public class SequenceDiagramToolScanConfig implements InitializingBean {

    @Resource
    private ToolScanUtils toolScanUtils;

    @Getter
    private final List<String> methodNames = new ArrayList<>();

    @Override
    public void afterPropertiesSet() {
        methodNames.addAll(toolScanUtils.findTool(DiagramConstant.TOOL_PACKAGE));
    }
}
