package com.tccc.meta.service;

import static java.util.stream.Collectors.groupingBy;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tccc.meta.domain.Errors;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ErrorsService {
  public static JsonObject parseErrors(Errors[] uniqueErrors) {
    JsonObject json = new JsonObject();
    if (uniqueErrors.length > 0) {
      json.addProperty("COUNTRY", uniqueErrors[0].getCOUNTRY());
      json.addProperty("COUNT", uniqueErrors.length);

      JsonArray fileNames = new JsonArray();
      Arrays.stream(uniqueErrors).map(Errors.class::cast).map(Errors::getFILE_NAME).distinct().forEach(fileNames::add);
      json.add("FILE_NAMES", fileNames);

      JsonArray errorCodes = new JsonArray();
      Arrays.stream(uniqueErrors).map(Errors.class::cast).map(Errors::getERROR_CODE).distinct().forEach(errorCodes::add);
      json.add("ERROR_CODES", errorCodes);

      Map<String, List<Errors>> errorsMap = Arrays.stream(uniqueErrors).map(Errors.class::cast).collect(groupingBy(Errors::getFILE_NAME));
      JsonArray files = new JsonArray();
      errorsMap.keySet().forEach(file -> {
            StringBuilder sb = new StringBuilder();
            errorsMap.get(file).forEach(er -> sb.append(er.getERROR_CODE() + ", "));
            sb.deleteCharAt(sb.length() - 2);
            sb.append("  error in  " + file);
            files.add(sb.toString());
          }
      );
      json.add("ERROR_MAPPING", files);
    } else {
      json.addProperty("COUNT", 0);
    }
    return json;
  }

}
