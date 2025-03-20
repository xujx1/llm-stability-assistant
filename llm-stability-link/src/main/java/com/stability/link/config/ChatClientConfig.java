package com.stability.link.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;

import com.stability.link.advisor.LoggingAdvisor;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClient.DefaultChatClientRequestSpec;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Resource
    private ToolScanConfig toolScanConfig;

    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder modelBuilder, VectorStore vectorStore, ChatMemory chatMemory) {

        return modelBuilder.defaultSystem(
                "您是一个资深的JAVA开发程序员，能够根据用户输入的类和方法简称，按照下面的步骤，产出完整的调用链路，并生成单元测试代码。\n"
                    + "  1、根据用户输入的类和方法简称，在本项目（当前java进程中）先判断类是否存在，如果存在找到对应的方法\n"
                    + "<方法解析示例>\n"
                    + "类似用户输入：com.stability.link.client.AssistantController类的chat方法，能够拆接触类名：com.stability.link.client"
                    + ".AssistantController和方法名chat\n"
                    + "</方法解析示例>\n"
                    + "  2、梳理整个方法和方法的内部调用的代码，如果当前方法内部依赖的类和方法在本项目（当前java"
                    + "进程中）依旧存在，那就重复当前流程，用被依赖的类、方法继续向下梳理依赖，最终返回业务链路相关的调用栈。\n"
                    + "特别要注意：过滤掉标准非业务相关的工具类，这是重点。\n"
                    + "最后返回的结果大概如下：\n"
                    + "<调用栈结果示例>\n"
                    + "  com.stability.link.client.BookingController#getBookings\n"
                    + "    com.stability.link.services.FlightBookingService.getBookings\n"
                    + "      com.stability.link.repo.BookingDataRepo.query\n"
                    + "</调用栈结果示例>\n"
                    + "  3、根据方法调用链路，生成uml时序图的代码\n"
                    + "<时序图代码示例>\n"
                    + "@startuml\n"
                    + "actor User\n"
                    + "participant \"BookingController\" as BC\n"
                    + "participant \"FlightBookingService\" as FBS\n"
                    + "\n"
                    + "User -> BC: getBookings()\n"
                    + "activate BC\n"
                    + "BC -> FBS: getBookings()\n"
                    + "activate FBS\n"
                    + "FBS --> BC: return bookings\n"
                    + "deactivate FBS\n"
                    + "BC --> User: return bookings\n"
                    + "deactivate BC\n"
                    + "@enduml"
                    + "</时序图代码示例>\n"
                    + "  4、根据业务链路相关的调用栈，生成所有业务链路上被调用方法的单元测试代码。\n"
            ).
            defaultAdvisors(
                // Chat Memory
                new PromptChatMemoryAdvisor(chatMemory),
                // RAG
                new QuestionAnswerAdvisor(vectorStore,
                    SearchRequest.builder().topK(4).similarityThresholdAll().build()),
                new LoggingAdvisor()).
            defaultFunctions(toolScanConfig.getMethodNames().toArray(new String[] {})).
            defaultOptions(DashScopeChatOptions.builder().withTopP(0.1).build()).build();
    }
}
