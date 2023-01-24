package com.tccc.meta.service;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tccc.meta.domain.CountryMapping;
import com.tccc.meta.domain.RenameMapping;

import java.util.*;

public class MetaParserService {
  
  private static boolean tokenFound(String text, String[] searchedTokens){
    for (String token : searchedTokens) {
      if (text.contains(token)){
        return true;
      }
    }
    return false;
  }

  private static String countryNameSearch(String projectName, String archiveName, CountryMapping[] countryMappings) {
    for (CountryMapping countryMapping : countryMappings) {
      String[] aliases = countryMapping.getALIASES();
      if(tokenFound(projectName, aliases) || tokenFound(archiveName, aliases)){
          return countryMapping.getCOUNTRY();
      }
    }
    throw new RuntimeException("Country name is empty in metafile. Define proper country mapping for projectName: " + projectName + " and archive name: " + archiveName + " in CosmosDB");
  }

  private static String renameCountry(String projectName, String archiveName, RenameMapping[] renameMappings, String countryName) {
    for (RenameMapping renameMapping : renameMappings) {
      String[] aliases = renameMapping.getALIASES();
      if(tokenFound(projectName, aliases) || tokenFound(archiveName, aliases)){
          return renameMapping.getRENAMED();
      }
    }
    //if no mapping for the country, then return original name of the country
    return countryName;
  }

  public static void fixCountryName(JsonObject json, CountryMapping[] countryMappings, RenameMapping[] renameMappings) {
    String countryName = json.get("COUNTRY").getAsString();
    String projectName = json.get("PROJECT_NAME").getAsString();
    String archiveName = json.get("ARCHIVE_NAME").getAsString();

    //fix country name if contains illegal characters
    countryName = countryName.toUpperCase()
            .replaceAll("[^A-Z_]", "_")
            .replaceAll("_+", "_")
            .replaceAll("_$", "")
            .replaceAll("^_", "");
    //rename some countries by mapping
    countryName = renameCountry(projectName, archiveName, renameMappings, countryName);
    //if country name is
    if (countryName.isEmpty()) {
      countryName = countryNameSearch(projectName, archiveName, countryMappings);
    }
    json.addProperty("COUNTRY", countryName);
  }

  public static String parser(String path, CountryMapping[] countryMappings, RenameMapping[] renameMappings) {
    String[] fileLines = path.split("\\n");
    JsonObject json = new JsonObject();
    Arrays.stream(fileLines)
            .forEach(line -> {
              if (line.contains(":")) {
                List<String> keyValuePairs = Splitter.onPattern(":").trimResults().splitToList(line);
                String key = keyValuePairs.get(0);
                String value = keyValuePairs.get(1);
                //here we should be careful. value can be cola; fanta or ';'
                if (value.contains(";")) {
                  //when value is ';'(especially when delimiter is ';')
                  if (value.contains("';'")) {
                    json.addProperty(key, value);
                  } else {
                    //split by ; when value is list of items like: cola;fanta;pepsi
                    List<String> valueList = Splitter.onPattern(";").omitEmptyStrings().trimResults().splitToList(value);
                    if (valueList.size() > 1) {
                      JsonArray jsonValueArray = new JsonArray();
                      valueList.forEach(e -> {
                        jsonValueArray.add(e);
                      });
                      json.add(key, jsonValueArray);
                    }
                  }
                } else {
                  json.addProperty(key, value);
                }
              }
            });
    fixCountryName(json, countryMappings, renameMappings);
    Gson gson = new GsonBuilder().create();

    return gson.toJson(json);
  }
}
