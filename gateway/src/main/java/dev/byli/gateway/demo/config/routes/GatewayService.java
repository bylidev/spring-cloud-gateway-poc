package dev.byli.gateway.demo.config.routes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GatewayService {
  private String url;
  private List<GatewayRoute> routes;
}
