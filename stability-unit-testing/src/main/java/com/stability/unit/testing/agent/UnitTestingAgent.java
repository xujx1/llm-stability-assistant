package com.stability.unit.testing.agent;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
public class UnitTestingAgent {

    @Resource
    private ChatClient sequenceDiagramChatClient;

    public Flux<String> chat(String chatId, String userMessageContent) {

        return sequenceDiagramChatClient.prompt()
            .user(userMessageContent)
            .advisors(a -> a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId).param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
            .stream()
            .content();
    }
}
