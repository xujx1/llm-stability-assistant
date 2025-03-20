package com.stability.agent;

import reactor.core.publisher.Flux;

public interface BaseAgent {

    Flux<String> chat(String chatId, String userMessageContent);
}
