package com.stability.client;

import java.util.List;

import com.stability.sequence.diagram.resp.MethodFieldResp;
import com.stability.sequence.diagram.services.MethodService;
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
