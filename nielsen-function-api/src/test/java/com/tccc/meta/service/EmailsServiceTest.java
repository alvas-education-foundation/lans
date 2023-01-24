package com.tccc.meta.service;

import com.tccc.meta.domain.Emails;
//import org.junit.Assert;
//import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class EmailsServiceTest {

  @Test
  public void parseEmails_succeed() {
    // Given
    Emails[] emails = {new Emails("Pilip_Varabyou@epam.com;jiuhuang@coca-cola.com"), new Emails("test@email.com")};
    String expectedEmails = "Pilip_Varabyou@epam.com;jiuhuang@coca-cola.com;test@email.com";
    // When
    String actualEmails = EmailsService.parseEmails(emails);

    // Then
    Assertions.assertNotNull(expectedEmails);
    Assertions.assertEquals(expectedEmails, actualEmails);
  }

}