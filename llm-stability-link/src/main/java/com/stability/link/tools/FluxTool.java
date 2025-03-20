package com.stability.link.tools;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

import com.stability.link.annotations.Tool;
import com.stability.link.stat.JCallGraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.ServerHttpResponse;
import reactor.core.publisher.Flux;

@Tool
@Slf4j
public class FluxTool {

    public record FileToFluxReq(String fileName, ServerHttpResponse response) {}

    @Bean
    @Description("读取磁盘的图片文件返回出客户端")
    public Function<FileToFluxReq, Void> fileToFluxReq() {
        return (request) -> {
            try (InputStream inputStream = Files.newInputStream(Paths.get(request.fileName))) {
                DataBufferFactory factory = new DefaultDataBufferFactory();
                 Flux.generate(sink -> {
                    DataBuffer buffer = factory.allocateBuffer(1024); // 分配一个1024字节的buffer
                    int bytesRead;
                    while (true) {
                        try {
                            if (!((bytesRead = inputStream.read(buffer.asByteBuffer().array())) != -1))
                                break;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        buffer.writePosition(bytesRead); // 更新写位置
                        sink.next(buffer.asByteBuffer().slice()); // 发送buffer的一部分并继续读取
                        buffer = factory.allocateBuffer(1024); // 重新分配buffer以继续读取
                    }
                    sink.complete(); // 完成流
                });

            } catch (IOException e) {

            }
            return null;
        };
    }
}
