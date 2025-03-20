package com.stability.link.client;

import java.util.List;

import com.stability.link.resp.MethodFieldResp;
import com.stability.link.services.MethodService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class MethodController {

    @Resource
    private MethodService methodService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/api/methods")
    @ResponseBody
    public List<MethodFieldResp> methods() {
        return methodService.getAllMethod();
    }
}
