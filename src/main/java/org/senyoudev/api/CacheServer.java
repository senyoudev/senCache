package org.senyoudev.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.senyoudev.config.CacheConfig;
import org.senyoudev.core.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheServer {

  private final Logger LOGGER = LoggerFactory.getLogger(CacheServer.class);
  private final CacheManager<String, String> cacheManager;
  private final HttpServer server;

  public CacheServer(int port) throws IOException {
    CacheConfig cacheConfig =
        CacheConfig.create()
            .addMaxSize(100)
            .addPolicyEviction(null)
            .addDataSource(null)
            .addSerializationType(null)
            .build();
    cacheManager = new CacheManager<>(cacheConfig);
    server = HttpServer.create(new InetSocketAddress(port), 0);

    server.createContext("/get", new GetHandler());
    server.createContext("/put", new PutHandler());
    server.createContext("/remove", new RemoveHandler());
    server.createContext("/clear", new ClearHandler());
  }

  public void start() {
    server.start();
    LOGGER.info("Server started on port {}", server.getAddress().getPort());
  }

  private class GetHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {}
  }

  private class PutHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {}
  }

  private class RemoveHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {}
  }

  private class ClearHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {}
  }
}
