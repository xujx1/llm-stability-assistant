package com.stability.sequence.diagram.services;

import java.util.List;

import com.stability.sequence.diagram.resp.MethodFieldResp;

public interface MethodService {

    /**
     * 获取所有方法
     *
     * @return
     */
    List<MethodFieldResp> getAllMethod();
}
