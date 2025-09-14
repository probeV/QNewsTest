package com.qraft.news.domain.news.config;

import com.qraft.news.domain.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final CustomerService customerService;

    // 토큰 정보를 저장할 속성 키
    private static final String CUSTOMER_TOKEN_ATTR = "customerToken";

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
                        log.info("WebSocket 연결 시도 - URI: {}",
                                request.getURI());

                        // 쿼리 파라미터에서 토큰 추출
                        String token = extractTokenFromRequest(request);

                        if(!customerService.validateTokenAndCheckConnection(token)) {   // 토큰 유효하지 않음, 중복 연결 등으로 웹 소켓 연결 차단
                            return false;
                        }

                        // 세션 attributes에 고객 토큰 저장
                        attributes.put(CUSTOMER_TOKEN_ATTR, token);

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

                    /**
                     * 쿼리 파라미터에서 토큰 추출
                     */
                    private String extractTokenFromRequest(ServerHttpRequest request) {
                        // 쿼리 파라미터에서 토큰 추출
                        String query = request.getURI().getQuery();
                        if (query != null) {
                            String[] params = query.split("&");
                            for (String param : params) {
                                if (param.startsWith("token=")) {
                                    return param.substring(6); // "token=" 제거
                                }
                            }
                        }

                        return null;
                    }
                })
                .withSockJS();
    }

    @Override
    public void  configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.addDecoratorFactory(webSocketHandler -> new WebSocketHandlerDecorator(webSocketHandler){
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                String token = session.getAttributes().get(CUSTOMER_TOKEN_ATTR).toString();
                customerService.activateConnection(token);

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
                String token = session.getAttributes().get(CUSTOMER_TOKEN_ATTR).toString();
                customerService.deactivateConnection(token);

                log.info("WebSocket 연결 종료 - SessionId: {}, CloseStatus: {} ({})",
                        session.getId(), closeStatus.getCode(), closeStatus.getReason());

                super.afterConnectionClosed(session, closeStatus);
            }

        });
    }
}
