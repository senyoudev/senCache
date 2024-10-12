package org.senyoudev.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.senyoudev.config.CacheConfig;
import org.senyoudev.core.CacheManager;
import org.senyoudev.datasource.FileDataSource;
import org.senyoudev.eviction.LRUEvictionPolicy;
import org.senyoudev.serialization.SerializationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheServer {

  private static final Logger LOGGER = LoggerFactory.getLogger(CacheServer.class);
  private final CacheManager<String, String> cacheManager;
  private final HttpServer server;
  private final int MAX_SIZE = 100;
  private final Path DATA_SOURCE = Path.of("data.json");

  public CacheServer(int port) throws IOException {
    CacheConfig cacheConfig =
        CacheConfig.create()
            .addMaxSize(MAX_SIZE)
            .addPolicyEviction(new LRUEvictionPolicy(MAX_SIZE))
            .addDataSource(new FileDataSource(DATA_SOURCE))
            .addSerializationType(SerializationType.JSON)
            .build();
    cacheManager = new CacheManager<>(cacheConfig);
    insertData();
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
    public void handle(HttpExchange exchange) throws IOException {
      LOGGER.info("Received GET request");
      if (!exchange.getRequestMethod().equals("GET")) {
        LOGGER.error("Invalid request method");
        exchange.sendResponseHeaders(405, -1);
        return;
      }

      Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
      String key = params.get("key");
      if (key == null) {
        LOGGER.error("Key not found in request");
        exchange.sendResponseHeaders(400, -1);
        return;
      }

      String value = cacheManager.get(key);
      if (value == null) {
        LOGGER.error("Key not found in cache");
        exchange.sendResponseHeaders(404, -1);
        return;
      }
      sendResponse(exchange, value);
    }
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

  /**
   * Parses the query string into a map. It takes a query like "key1=value1&key2=value2" and returns
   * a map with the keys and values like {key1: value1, key2: value2}.
   */
  private Map<String, String> queryToMap(String query) {
    Map<String, String> result = new HashMap<>();
    if (query != null) {
      for (String param : query.split("&")) {
        String[] pair = param.split("=");
        if (pair.length > 1) {
          result.put(pair[0], pair[1]);
        } else {
          result.put(pair[0], "");
        }
      }
    }
    return result;
  }

  private void sendResponse(HttpExchange exchange, String response) throws IOException {
    exchange.sendResponseHeaders(200, response.length());
    OutputStream os = exchange.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }

  public static void main(String[] args) throws IOException {
    CacheServer server = new CacheServer(8080);
    LOGGER.info("Starting server");
    server.start();
  }

  private void insertData() {
    cacheManager.put("key1", "value1");
    cacheManager.put("key2", "value2");
    cacheManager.put("key3", "value3");
    cacheManager.put("key4", "value4");
  }
}
