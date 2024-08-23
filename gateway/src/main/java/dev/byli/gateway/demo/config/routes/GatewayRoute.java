package dev.byli.gateway.demo.config.routes;

import io.swagger.v3.oas.models.PathItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GatewayRoute {
  private String path;
  private String redirect;
  private PathItem.HttpMethod method;
  private boolean cached;
}
