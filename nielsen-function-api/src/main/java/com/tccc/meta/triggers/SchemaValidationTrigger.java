package com.tccc.meta.triggers;

import static com.tccc.meta.service.SchemaValidationService.getMissedColumns;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.CosmosDBInput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Map;
import java.util.Optional;

/**
 * HTTP trigger function that listens at endpoint "/api/schema".
 */
public class SchemaValidationTrigger {

  @FunctionName("schema")
  public String run(
      @HttpTrigger(name = "validateSchema",
          methods = {HttpMethod.POST},
          authLevel = AuthorizationLevel.ANONYMOUS,
          route = "schema")
          HttpRequestMessage<Optional<String>> request,
      @CosmosDBInput(name = "database",
          databaseName = "%COSMOS-DB-NAME%",
          collectionName = "schemas",
          sqlQuery = "select * from c where c.COUNTRY = {COUNTRY}",
          connectionStringSetting = "COSMOS-DB-CONNECTION-STRING")
          String[] cosmosSchemas,
      final ExecutionContext context) {

    context.getLogger().info("Payload received is " + request.getBody());

    Map<String, String> missedColumns = getMissedColumns(request, cosmosSchemas);

    JsonObject json = new JsonObject();
    JsonObject errors = new JsonObject();
    for (String missedFile : missedColumns.keySet()) {
      errors.addProperty(missedFile, missedColumns.get(missedFile));
    }
    json.addProperty("COUNT", missedColumns.size());
    json.add("ERRORS", errors);
    json.addProperty("ERROR_CODES", "E-005");


    Gson gson = new GsonBuilder().create();
    return gson.toJson(json);

  }
}


