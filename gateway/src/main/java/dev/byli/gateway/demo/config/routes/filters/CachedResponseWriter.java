package dev.byli.gateway.demo.config.routes.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cache.Cache;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class CachedResponseWriter implements RewriteFunction<String, String> {
  private final Cache cache;

  @Override
  public Publisher<String> apply(ServerWebExchange exchange, String originalRequest) {
    String path = exchange.getRequest().getPath().toString();
    ServerHttpResponse response = exchange.getResponse();
    CachedResponse cachedResponse = new CachedResponse();
    cachedResponse.setBody(originalRequest);
    cachedResponse.setHeaders(response.getHeaders());
    cache.put(path, cachedResponse);
    log.debug("Response body cached for path: {}", path);
    return Mono.just(originalRequest);
  }
}
