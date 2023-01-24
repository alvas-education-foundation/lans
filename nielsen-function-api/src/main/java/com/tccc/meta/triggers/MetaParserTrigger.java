package com.tccc.meta.triggers;


import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.*;
import com.tccc.meta.domain.CountryMapping;
import com.tccc.meta.domain.RenameMapping;
import com.tccc.meta.domain.ParserRequest;
import com.tccc.meta.service.MetaParserService;

import java.util.Optional;

public class MetaParserTrigger {

  /**
   * HTTP trigger function that listens at endpoint "/api/parse".
   */
  @FunctionName("parse")
  @StorageAccount("BlobStorage")
  public HttpResponseMessage run(
          @HttpTrigger(name = "parse", methods = HttpMethod.POST, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<ParserRequest>> request,
          @BlobInput(
                  name = "blob",
                  dataType = "string",
                  path = "{path}")
                  String content,
          @CosmosDBInput(name = "database",
                  databaseName = "%COSMOS-DB-NAME%",
                  collectionName = "countries",
                  sqlQuery = "select c.COUNTRY, c.ALIASES from c",
                  connectionStringSetting = "COSMOS-DB-CONNECTION-STRING")
                  CountryMapping[] cosmosCountryMappings,
          @CosmosDBInput(name = "renamedMappings",
                  databaseName = "%COSMOS-DB-NAME%",
                  collectionName = "renamed_countries",
                  sqlQuery = "select c.RENAMED, c.ALIASES from c",
                  connectionStringSetting = "COSMOS-DB-CONNECTION-STRING")
                  RenameMapping[] cosmosRenameMappings,
          final ExecutionContext context) {
    context.getLogger().info("HTTP trigger processed a request:" + request.getBody());

    String json = MetaParserService.parser(content, cosmosCountryMappings, cosmosRenameMappings);

    return request.createResponseBuilder(HttpStatus.OK)
            .header("Content-Type", "application/json")
            .body(json)
            .build();
  }
}
