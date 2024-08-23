package dev.byli.gateway.demo.config.routes.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_PREDICATE_PATH_CONTAINER_ATTR;

@Slf4j
@Component
public class CacheFilter implements GatewayFilter {

  Cache cache;

  public CacheFilter(Cache cache) {
    this.cache = cache;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

    PathContainer path = exchange.getAttribute(GATEWAY_PREDICATE_PATH_CONTAINER_ATTR);
    CachedResponse cachedResponse = cache.get(path.value(), CachedResponse.class);

    if (cachedResponse == null) {
      log.debug("No cache found for path: {}", path);
      return chain.filter(exchange);
    }

    log.debug("Cache found for path: {}", path);
    ServerHttpResponse response = exchange.getResponse();
    response.getHeaders().addAll(cachedResponse.getHeaders());
    DataBuffer dataBuffer =
        response.bufferFactory().wrap(cachedResponse.getBody().getBytes(StandardCharsets.UTF_8));
    return response.writeWith(Flux.just(dataBuffer)).then(Mono.empty());
  }
}
