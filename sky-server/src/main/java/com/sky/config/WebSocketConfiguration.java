package com.sky.config;

import com.sky.websocket.WebSocketServer;
import org.springframework.context.annotation.Bean;

public class WebSocketConfiguration {
    @Bean
    public WebSocketServer webSocketServer(){
        return new WebSocketServer();
    }
}
