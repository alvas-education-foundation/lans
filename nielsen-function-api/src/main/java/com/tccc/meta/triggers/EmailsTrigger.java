package com.tccc.meta.triggers;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.CosmosDBInput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.tccc.meta.domain.Emails;
import com.tccc.meta.service.EmailsService;

import java.util.Optional;

/**
 * HTTP trigger function that listens at endpoint "/api/emails".
 */
public class EmailsTrigger {
  @FunctionName("emails")
  public String run(
      @HttpTrigger(name = "getEmails",
          methods = {HttpMethod.POST},
          authLevel = AuthorizationLevel.ANONYMOUS,
          route = "emails")
          HttpRequestMessage<Optional<String>> request,
      @CosmosDBInput(name = "database",
          databaseName = "%COSMOS-DB-NAME%",
          collectionName = "emails",
          sqlQuery = "select c.RECIPIENT from c where c.COUNTRY = 'GLOBAL' or c.COUNTRY = {COUNTRY}",
          connectionStringSetting = "COSMOS-DB-CONNECTION-STRING")
          Emails[] cosmosResponse,
      final ExecutionContext context) {

    return EmailsService.parseEmails(cosmosResponse);

  }
}

