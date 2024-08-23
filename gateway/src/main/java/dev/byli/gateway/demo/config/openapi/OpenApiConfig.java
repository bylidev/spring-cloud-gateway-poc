package dev.byli.gateway.demo.config.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import dev.byli.gateway.demo.config.routes.GatewayRoutesLoader;
@OpenAPIDefinition
@Configuration
@DependsOn("routerLocatorFactory")
@RequiredArgsConstructor
public class OpenApiConfig {
  private final GatewayRoutesLoader gatewayRoutesLoader;

  @Bean
  public OpenAPI balanceOpenApi() {

    return new OpenAPI()
        .info(
            new Info()
                .title("dummy")
                .description("infoDescription")
                .version("infoVersion")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
        .paths(getGatewayPaths())
        .externalDocs(new ExternalDocumentation().description("docsDescription").url("docsUrl"));
  }

  private Paths getGatewayPaths() {
    var paths = new Paths();
    gatewayRoutesLoader
        .getServices()
        .forEach(
            (serviceName, base) -> {
              base.getRoutes()
                  .forEach(
                      route -> {
                        paths.addPathItem(
                            route.getRedirect(),
                            getFrom(
                                route.getMethod(),
                                new Operation().summary(serviceName).addTagsItem(serviceName)));
                      });
            });
    return paths;
  }

  private PathItem getFrom(PathItem.HttpMethod method, Operation operation) {
    return switch (method) {
      case GET -> new PathItem().get(operation);
      case POST -> new PathItem().post(operation);
      case PUT -> new PathItem().put(operation);
      case DELETE -> new PathItem().delete(operation);
      case PATCH -> new PathItem().patch(operation);
      case HEAD -> new PathItem().head(operation);
      case OPTIONS -> new PathItem().options(operation);
      default -> throw new IllegalStateException("Unexpected value: " + method);
    };
  }
}
