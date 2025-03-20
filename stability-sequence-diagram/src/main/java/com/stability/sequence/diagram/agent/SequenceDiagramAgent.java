package com.stability.sequence.diagram.agent;

import com.stability.agent.BaseAgent;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
@Description("生成时序图、uml图的助手")
public class SequenceDiagramAgent implements BaseAgent {

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
