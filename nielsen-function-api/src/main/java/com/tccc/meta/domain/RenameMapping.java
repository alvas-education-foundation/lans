package com.tccc.meta.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class RenameMapping {
  String RENAMED;
  String[] ALIASES;
}