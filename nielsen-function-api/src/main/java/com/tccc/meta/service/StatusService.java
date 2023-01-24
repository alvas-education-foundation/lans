package com.tccc.meta.service;

import static com.tccc.meta.utils.StatusUtils.createJsonNode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StatusService {

  public static JsonNode updateExistingDocument(JsonObject bodyJson, Set<String> cosmosCategories, String category,
      String[] status) throws JsonProcessingException {
    Set<String> processedCategories = new HashSet<>();
    processedCategories.add(category);
    StreamSupport
        .stream(new ObjectMapper().readTree(status[0]).get("category_status").spliterator(), false)
        .collect(Collectors.toList())
        .forEach(cat -> {
          if (cat.get("is_processed").asBoolean()) {
            processedCategories.add(cat.get("category").asText());
          }
        });
    return createJsonNode(bodyJson, cosmosCategories, processedCategories);
  }

  public static JsonNode createNewDocumnet(JsonObject bodyJson, Set<String> cosmosCategories, String category) {
    return createJsonNode(bodyJson, cosmosCategories, new HashSet<>(ImmutableList.of(category)));
  }

  public static HttpResponseMessage badRequest(HttpRequestMessage<Optional<String>> request) {
    return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
        .header("Content-Type", "application/json")
        .body("Bad Request")
        .build();
  }
}
