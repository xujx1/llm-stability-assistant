package com.stability.client;

import com.stability.sequence.diagram.agent.SequenceDiagramAgent;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequestMapping("/api/assistant")
@RestController
public class AssistantController {

    @Resource
    private SequenceDiagramAgent sequenceDiagramAgent;

    @RequestMapping(path = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(String chatId, String userMessage) {
        return sequenceDiagramAgent.chat(chatId, userMessage);
    }

}
