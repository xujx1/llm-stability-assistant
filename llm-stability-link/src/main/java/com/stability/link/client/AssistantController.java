package com.stability.link.client;

import com.stability.link.agent.StabilityLinkAgent;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequestMapping("/api/assistant")
@RestController
public class AssistantController {

    @Resource
    private StabilityLinkAgent stabilityLinkAgent;

    @RequestMapping(path = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(String chatId, String userMessage) {
        return stabilityLinkAgent.chat(chatId, userMessage);
    }

}
