package com.sky.Task;

import com.sky.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class WebSocketTask {
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 通过webSocket每隔5秒向客户端发送信息
     *
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void sendMessageToAllClient() {
        webSocketServer.sendToAllClient("这是来自服务端的信息"+ DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
    }
}
