package com.stability.link.services;

import java.util.List;

import com.stability.link.resp.MethodFieldResp;

public interface MethodService {

    /**
     * 获取所有方法
     *
     * @return
     */
    List<MethodFieldResp> getAllMethod();
}
