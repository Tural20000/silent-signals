package com.abdullayevtural.silent_signals.config;

import java.security.Principal;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.abdullayevtural.silent_signals.websocket.JwtHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

	public WebSocketConfig(JwtHandshakeInterceptor jwtHandshakeInterceptor) {
		this.jwtHandshakeInterceptor = jwtHandshakeInterceptor;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic");
		registry.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		DefaultHandshakeHandler handshakeHandler = new DefaultHandshakeHandler() {
			@Override
			protected Principal determineUser(org.springframework.http.server.ServerHttpRequest request,
					WebSocketHandler wsHandler, Map<String, Object> attributes) {
				Object p = attributes.get("principal");
				if (p instanceof Principal principal) {
					return principal;
				}
				return null;
			}
		};

		registry.addEndpoint("/ws").setHandshakeHandler(handshakeHandler).addInterceptors(jwtHandshakeInterceptor)
				.setAllowedOriginPatterns("http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:5500",
						"http://localhost:8011")
				.withSockJS();
	}
}
