package dev.byli.gateway.demo.config.routes;

import dev.byli.gateway.demo.config.routes.filters.CacheFilter;
import dev.byli.gateway.demo.config.routes.filters.CachedResponseWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@EnableConfigurationProperties({GatewayRoutesLoader.class})
@RequiredArgsConstructor
@Configuration
public class RouterLocatorFactory {

  private final CacheFilter cacheFilter;
  private final CachedResponseWriter cachedResponse;

  @Bean
  public RouteLocator staticRoutes(
      GatewayRoutesLoader gatewayRoutesLoader, RouteLocatorBuilder builder) {
    RouteLocatorBuilder.Builder routesBuilder = builder.routes();

    gatewayRoutesLoader
        .getServices()
        .forEach(
            (serviceName, service) -> {
              log.info("Registering service: {}", serviceName);
              service
                  .getRoutes()
                  .forEach(
                      route -> {
                        log.info("route: {}", route);
                        registerRoute(routesBuilder, service.getUrl(), route);
                      });
            });

    return routesBuilder.build();
  }

  private void registerRoute(
      RouteLocatorBuilder.Builder routesBuilder, String baseUrl, GatewayRoute route) {
    routesBuilder.route(
        routeId ->
            routeId
                .path(route.getRedirect())
                .and()
                .method(route.getMethod().name())
                .filters(f -> applyFilters(f, route))
                .uri(baseUrl));
  }

  private GatewayFilterSpec applyFilters(GatewayFilterSpec filterSpec, GatewayRoute route) {
    filterSpec.setPath(route.getPath());

    if (route.isCached()) {
      filterSpec.filter(cacheFilter).modifyResponseBody(String.class, String.class, cachedResponse);
    }

    return filterSpec;
  }
}
