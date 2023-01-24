package com.tccc.meta.triggers;

import static com.tccc.meta.builder.HTMLTableBuilder.buildHTMLTable;
import static com.tccc.meta.service.StatusService.badRequest;
import static com.tccc.meta.service.StatusService.createNewDocumnet;
import static com.tccc.meta.service.StatusService.updateExistingDocument;
import static com.tccc.meta.utils.StatusUtils.extractCategories;
import static com.tccc.meta.utils.StatusUtils.isValid;
import static com.tccc.meta.utils.StatusUtils.upsertToCosmos;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.CosmosDBInput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

/**
 * HTTP trigger function that listens at endpoint "/api/status".
 */
public class StatusTrigger {

  @FunctionName("status")
  public HttpResponseMessage run(
      @HttpTrigger(name = "updateStatus",
          methods = {HttpMethod.POST},
          authLevel = AuthorizationLevel.ANONYMOUS,
          route = "status")
          HttpRequestMessage<Optional<String>> request,
      @CosmosDBInput(name = "configuration",
          databaseName = "%COSMOS-DB-NAME%",
          collectionName = "configuration",
          sqlQuery = "select c.MAPPING from c where c.COUNTRY = {COUNTRY}",
          connectionStringSetting = "COSMOS-DB-CONNECTION-STRING")
          String[] categoryMapping,
      @CosmosDBInput(name = "status",
          databaseName = "%COSMOS-DB-NAME%",
          collectionName = "status",
          sqlQuery = "select c.processed_period, c.category_status, c.id from c where c.processed_period = {PROCESSED_PERIOD} AND c.id = {COUNTRY}",
          connectionStringSetting = "COSMOS-DB-CONNECTION-STRING")
          String[] status,
      final ExecutionContext context) throws DocumentClientException, IOException {

    context.getLogger().info("Payload received is " + request.getBody());

    JsonParser jsonParser = new JsonParser();
    JsonObject bodyJson = jsonParser.parse(request.getBody().orElse("")).getAsJsonObject();
    Set<String> cosmosCategories = extractCategories(jsonParser, categoryMapping);
    String category = bodyJson.get("CATEGORY").getAsString();

    if (!isValid(bodyJson, cosmosCategories, status)) return badRequest(request);

    JsonNode jsonNode = null;
    if (ArrayUtils.isEmpty(status)) {
      jsonNode = createNewDocumnet(bodyJson, cosmosCategories, category);
    } else {
      jsonNode = updateExistingDocument(bodyJson, cosmosCategories, category, status);
    }

    upsertToCosmos(jsonNode);
    String htmlTABLE = buildHTMLTable(jsonNode);


    return request.createResponseBuilder(HttpStatus.OK)
        .header("Content-Type", "application/json")
        .body(htmlTABLE)
        .build();
  }

}


