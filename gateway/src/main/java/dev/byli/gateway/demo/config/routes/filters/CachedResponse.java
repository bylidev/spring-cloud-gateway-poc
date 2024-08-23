package dev.byli.gateway.demo.config.routes.filters;

import org.springframework.util.MultiValueMap;

public class CachedResponse {
  private String body;
  private MultiValueMap<String, String> headers;

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public MultiValueMap<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(MultiValueMap<String, String> headers) {
    this.headers = headers;
  }
}
