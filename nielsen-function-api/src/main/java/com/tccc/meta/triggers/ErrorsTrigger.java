package com.tccc.meta.triggers;

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
import com.tccc.meta.domain.CosmosResponse;
import com.tccc.meta.domain.Errors;
import com.tccc.meta.service.ErrorsService;

import java.util.Arrays;
import java.util.Optional;

/**
 * HTTP trigger function that listens at endpoint "/api/errors".
 */
public class ErrorsTrigger {
  @FunctionName("errors")
  public String run(
      @HttpTrigger(name = "getErrors",
          methods = {HttpMethod.POST},
          authLevel = AuthorizationLevel.ANONYMOUS,
          route = "errors")
          HttpRequestMessage<Optional<String>> request,
      @CosmosDBInput(name = "database",
          databaseName = "%COSMOS-DB-NAME%",
          collectionName = "errors",
          sqlQuery = "select c.ERROR_CODE, c.COUNTRY, c.FILE_NAME from c where c.RUN_ID = {RUN_ID}",
          connectionStringSetting = "COSMOS-DB-CONNECTION-STRING")
          CosmosResponse[] cosmosResponse,
      final ExecutionContext context) {

    // errors list
    Errors[] uniqueErrors = Arrays.stream(cosmosResponse).map(CosmosResponse::getPayload).distinct().toArray(Errors[]::new);
    context.getLogger().info("Parameters are: " + request.getQueryParameters());
    context.getLogger().info("Number of items from the database is " + (uniqueErrors == null ? 0 : uniqueErrors.length));

    JsonObject json = ErrorsService.parseErrors(uniqueErrors);
    Gson gson = new GsonBuilder().create();

    return gson.toJson(json);
  }
}
