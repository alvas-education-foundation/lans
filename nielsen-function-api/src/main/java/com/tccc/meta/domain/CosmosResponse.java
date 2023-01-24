package com.tccc.meta.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CosmosResponse {
  Item[] groupByItems;
  Errors payload;
}