package com.qraft.news.domain.news.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/ws/news");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request,
                                                   ServerHttpResponse response,
                                                   WebSocketHandler wsHandler,
                                                   Map<String, Object> attributes) throws Exception {
                        log.info("WebSocket 연결 시도 - URI: {}, Origin: {}",
                                request.getURI(), request.getHeaders().getOrigin());
                        return true;
                    }

                    @Override
                    public void afterHandshake(ServerHttpRequest request,
                                               ServerHttpResponse response,
                                               WebSocketHandler wsHandler,
                                               Exception exception) {
                        if (exception != null) {
                            log.error("WebSocket 연결 실패 - URI: {}, Error: {}",
                                    request.getURI(), exception.getMessage(), exception);
                        } else {
                            log.info("WebSocket handshake 완료 - URI: {}", request.getURI());
                        }
                    }
                })
                .withSockJS();
    }

    @Override
    public void  configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.addDecoratorFactory(webSocketHandler -> new WebSocketHandlerDecorator(webSocketHandler){
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                log.info("WebSocket 연결 성공 - SessionId: {}, RemoteAddress: {}",
                        session.getId(), session.getRemoteAddress());
                super.afterConnectionEstablished(session);
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                log.error("WebSocket 전송 중 오류 발생 - SessionId: {}, Error: {}",
                        session.getId(), exception.getMessage(), exception);
                super.handleTransportError(session, exception);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                log.info("WebSocket 연결 종료 - SessionId: {}, CloseStatus: {} ({})",
                        session.getId(), closeStatus.getCode(), closeStatus.getReason());
                super.afterConnectionClosed(session, closeStatus);
            }

        });
    }
}
