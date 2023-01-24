package com.tccc.meta.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @EqualsAndHashCode
public class Errors {

  private String COUNTRY;
  private String ERROR_CODE;
  private String FILE_NAME;
  private String RUN_ID;
}
