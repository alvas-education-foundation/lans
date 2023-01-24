package com.tccc.meta.service;

import com.tccc.meta.domain.Emails;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EmailsService {
  public static String parseEmails(Emails[] cosmosResponse) {
    return Arrays.stream(cosmosResponse)
        .map(Emails::getRECIPIENT)
        .flatMap(email -> Arrays.stream(email.split(";")))
        .map(e -> e.replace(";", ""))
        .collect(Collectors.joining(";"));
  }
}
