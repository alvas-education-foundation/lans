package com.tccc.meta.service;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.microsoft.azure.functions.HttpRequestMessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public class SchemaValidationService {

  private static final ImmutableSortedMap<String, String> TABLE_MAPPING = new ImmutableSortedMap
      .Builder<String, String>(Ordering.natural())
      .put("fact_data", "DATA")
      .put("PROD", "PROD")
      .put("FCT", "FCT")
      .put("PER", "PER")
      .put("MKT", "MKT")
      .build();

  /**
   * COUNTRY column is added during ETL processing and is added to exceptions
   * to avoid column duplications
   */
  private static final ImmutableSet<String> COLUMNS_EXCEPTIONS = new ImmutableSet
      .Builder<String>()
      .add("COUNTRY")
      .build();

  public static Map<String, String> getMissedColumns(HttpRequestMessage<Optional<String>> request, String[] cosmosSchemas) {
    Map<String, String> missedColumns = new HashMap<>();

    JsonParser jsonParser = new JsonParser();
    Map<String, Set<String>> cosmosColumnsMap = new HashMap<>();
    Arrays.stream(cosmosSchemas).forEach(schema -> {
      String fileType = jsonParser.parse(schema).getAsJsonObject().get("FILE_TYPE").getAsString();
      Set<String> cosmosColumnNames = jsonParser.parse(schema).getAsJsonObject().get("SCHEMA").getAsJsonObject().keySet();
      cosmosColumnsMap.put(fileType, cosmosColumnNames);
    });

    JsonArray files = jsonParser.parse(request.getBody().orElse("")).getAsJsonObject().get("FILES").getAsJsonArray();
    String delimeter = jsonParser.parse(request.getBody().orElse("")).getAsJsonObject().get("DELIMETER").getAsString()
        .replace("'", "");
    files.forEach(file -> {
      String parsedFileName = file.getAsJsonObject().get("file_name").getAsString();
      String parsedFileHeader = file.getAsJsonObject().get("header").getAsString();

      Set<String> parsedColumns = parseColumns(parsedFileHeader, delimeter);
      Set<String> cosmosColumns = cosmosColumnsMap.get(findFileNameKey(parsedFileName));

      for (String parsedColumn : parsedColumns) {
        if (!isEmpty(cosmosColumns) && !cosmosColumns.contains(parsedColumn)
            && !COLUMNS_EXCEPTIONS.contains(parsedColumn)) missedColumns
            .merge(parsedFileName, parsedColumn, (a, b) -> a + "# " + b);
      }
    });

    return missedColumns;
  }

  public static String findFileNameKey(String parsedFileName) {
    for (String fileName : TABLE_MAPPING.keySet()) {
      if (parsedFileName.contains(fileName)) return TABLE_MAPPING.get(fileName);
    }
    return EMPTY;
  }

  public static Set<String> parseColumns(String json, String delimeter) {
    return Sets.newHashSet(json.substring(8, json.length() - 1).split(Pattern.quote(delimeter)));
  }
}
