package com.tccc.meta.utils;

import static java.lang.System.getProperty;
import static java.lang.System.getenv;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.RequestOptions;

import java.util.HashSet;
import java.util.Set;

public class StatusUtils {

private static final String MASTER_KEY = getProperty("MASTER-KEY",
    defaultString(trimToNull(getenv().get("MASTER-KEY"))));
  private static final String COSMOS_ENDPOINT = getProperty("COSMOS-ENDPOINT",
      defaultString(trimToNull(getenv().get("COSMOS-ENDPOINT"))));
  private static final String COSMOS_DB_NAME = getProperty("COSMOS-DB-NAME",
      defaultString(trimToNull(getenv().get("COSMOS-DB-NAME"))));
  private static DocumentClient client = new DocumentClient(COSMOS_ENDPOINT, MASTER_KEY,
      new ConnectionPolicy(), ConsistencyLevel.Session);
  private static String collectionLink = String.format("/dbs/%s/colls/%s", COSMOS_DB_NAME, "status");

  public static Boolean isValid(JsonObject bodyJson, Set<String> categories, String[] status) {
    String country = bodyJson.get("COUNTRY").getAsString();
    String category = bodyJson.get("CATEGORY").getAsString();
    String processedDate = bodyJson.get("PROCESSED_PERIOD").getAsString();
    return isValidCountry(country) && isValidCategory(category, categories) && isValidProcessedDate(processedDate);
  }

  private static boolean isValidProcessedDate(String processedDate) {
    return isNotBlank(processedDate);
  }

  private static boolean isValidCategory(String category, Set<String> categories) {
    return isNotBlank(category) && categories.contains(category);
  }

  private static Boolean isValidCountry(String country) {
    return isNotBlank(country);
  }

  public static Set<String> extractCategories(JsonParser jsonParser, String[] categoryMapping) {
    Set<String> categories = new HashSet<>();
    JsonObject jsonCategories = jsonParser.parse(categoryMapping[0]).getAsJsonObject().get("MAPPING").getAsJsonObject();
    jsonCategories.keySet().forEach(category -> {
      String categoryValue = jsonCategories.get(category).getAsJsonObject().get("value").getAsString();
      if (!categoryValue.equalsIgnoreCase("UNDEFINED")) {
        categories.add(categoryValue);
      }
    });
    return categories;
  }

  public static JsonNode createJsonNode(JsonObject bodyJson, Set<String> cosmosCategories, Set<String> processedCategories) {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode = mapper.createObjectNode();
    String country = bodyJson.get("COUNTRY").getAsString();
    String processedDate = bodyJson.get("PROCESSED_PERIOD").getAsString();

    ((ObjectNode) rootNode).put("processed_period", processedDate);
    ((ObjectNode) rootNode).put("id", country);
    ArrayNode categoryList = mapper.createArrayNode();
    cosmosCategories.forEach(category -> {
      JsonNode node = mapper.createObjectNode();
      if (processedCategories.contains(category)) {
        ((ObjectNode) node).put("is_processed", true);
        ((ObjectNode) node).put("category", category);
      } else {
        ((ObjectNode) node).put("is_processed", false);
        ((ObjectNode) node).put("category", category);
      }
      categoryList.add(node);
    });
    ((ObjectNode) rootNode).set("category_status", categoryList);

    return rootNode;
  }

  public static void upsertToCosmos(JsonNode rootNode) throws DocumentClientException {
    client.upsertDocument(collectionLink, rootNode, new RequestOptions(), true);
  }
}
