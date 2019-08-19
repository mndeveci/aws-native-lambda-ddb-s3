package com.mndeveci;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class NativeExecutor {

  static {
    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
  }

  public static void main(String[] args) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    final String requestId = args[0];
    final String requestBody = args[1];
    final String responseFile = args[2];
    final String errorFile = args[3];

    final PersonRequest personRequest;
    final PersonResponse personResponse;
    try {
      personRequest = objectMapper.readValue(requestBody, PersonRequest.class);
      personResponse = new SavePersonHandler().handleRequest(personRequest, null);
    } catch (Exception e) {
      e.printStackTrace();

      Files.write(Paths.get(errorFile), "ERROR".getBytes(StandardCharsets.UTF_8));
      return;
    }

    StringWriter response = new StringWriter();
    objectMapper.writeValue(response, personResponse);
    Files.write(Paths.get(responseFile), response.toString().getBytes(StandardCharsets.UTF_8));
  }

}

