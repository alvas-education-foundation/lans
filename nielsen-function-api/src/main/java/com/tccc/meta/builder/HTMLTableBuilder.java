package com.tccc.meta.builder;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class HTMLTableBuilder {

  private int columns;
  private final StringBuilder table = new StringBuilder();
  public static String HTML_START = "<html>";
  public static String HTML_END = "</html>";
  public static String TABLE_START = "<table>";
  public static String TABLE_END = "</table>";
  public static String HEADER_START = "<th>";
  public static String HEADER_END = "</th>";
  public static String ROW_START = "<tr>";
  public static String ROW_END = "</tr>";
  public static String COLUMN_START = "<td>";
  public static String COLUMN_END = "</td>";
  public static String STYLE = "<style>table{font-family:arial,sans-serif;border-collapse:collapse;width:100%;}td,th{border:1pxsolid#dddddd;text-align:left;padding:8px;}tr:nth-child(even){background-color:#dddddd;}</style>";

  public HTMLTableBuilder(String header, int columns) {
    this.columns = columns;
    if (header != null) {
      table.append("<b>");
      table.append(header);
      table.append("</b>");
    }
    table.append(HTML_START);
    table.append(STYLE);
    table.append(TABLE_START);
    table.append(TABLE_END);
    table.append(HTML_END);
  }

  public void addTableHeader(String... values) {
    if (values.length != columns) {
      System.out.println("Error column lenth");
    } else {
      int lastIndex = table.lastIndexOf(TABLE_END);
      if (lastIndex > 0) {
        StringBuilder sb = new StringBuilder();
        sb.append(ROW_START);
        for (String value : values) {
          sb.append(HEADER_START);
          sb.append(value);
          sb.append(HEADER_END);
        }
        sb.append(ROW_END);
        table.insert(lastIndex, sb.toString());
      }
    }
  }

  public void addRowValues(String... values) {
    if (values.length != columns) {
      System.out.println("Error column lenth");
    } else {
      int lastIndex = table.lastIndexOf(ROW_END);
      if (lastIndex > 0) {
        int index = lastIndex + ROW_END.length();
        StringBuilder sb = new StringBuilder();
        sb.append(ROW_START);
        for (String value : values) {
          sb.append(COLUMN_START);
          sb.append(value);
          sb.append(COLUMN_END);
        }
        sb.append(ROW_END);
        table.insert(index, sb.toString());
      }
    }
  }

  public String build() {
    return table.toString();
  }


  public static String buildHTMLTable(JsonNode json) {
    HTMLTableBuilder htmlBuilder = new HTMLTableBuilder(null, 3);
    htmlBuilder.addTableHeader(" category ", " processed_period ", " is_processed ");
    String processedPeriod = json.get("processed_period").asText();
    StreamSupport
        .stream(json.get("category_status").spliterator(), false)
        .collect(Collectors.toList())
        .forEach(cat -> {
          htmlBuilder.addRowValues(cat.get("category").asText(), processedPeriod, cat.get("is_processed").asText());
        });
    return htmlBuilder.build();
  }
}