package dev.byli.gateway.demo.config.routes;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "gateway")
@Getter
@Setter
public class GatewayRoutesLoader {
  private Map<String, GatewayService> services;
}
