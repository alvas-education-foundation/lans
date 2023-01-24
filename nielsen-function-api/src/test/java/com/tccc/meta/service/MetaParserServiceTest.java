package com.tccc.meta.service;


import com.tccc.meta.domain.CountryMapping;
import com.tccc.meta.domain.RenameMapping;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
//import org.junit.Assert;
//import org.junit.Test;

public class MetaParserServiceTest {


  @Test
  public void parser_succeed() {
    // Given
    String metaFile = "################################ METADATA ################################\n" +
            "\n" +
            "VERSION                         :         2.0\n" +
            "DELIMITER                       :         '|'\n" +
            "ENCODING_USED                   :         ASCII\n" +
            "CHARACTER_SET_USED              :         UTF8\n" +
            "NEW_LINE_FEED_CHAR              :         '\\r\\n'\n" +
            "NUM_OF_DIMENSIONS               :         4\n" +
            "\n" +
            "################################ TRANSACTION DETAILS ################################\n" +
            "\n" +
            "RECORD_ID                       :         Hydration\n" +
            "PROJECT_NAME                    :         RF_20997_175COKE_20191410\n" +
            "COUNTRY                         :         CHINA\n" +
            "CATEGORY                        :         Functional Drink ; \n" +
            "LAST_PERIOD                     :         MONTH - OCT19 ; \n" +
            "DATE_TIME(WITH TIMEZONE)EXPORT  :         20191204 11:48:19 China Standard Time\n" +
            "DATA_CENTRE_LOCATION            :         RF\n" +
            "ARCHIVE_NAME                    :         Hydration_arch_12042019.ZIP\n" +
            "ARCHIVE_SIZE                    :         289.037 MB\n" +
            "NUM_OF_FILES                    :         7\n" +
            "FILE_NAMES                      :         Hydration_MKT_12042019.csv; Hydration_PROD_12042019.csv; Hydration_FCT_12042019.csv; Hydration_PER_12042019.csv; Hydration_FACT_DATA_12042019.csv; Hydration_DIM_12042019.csv; Hydration_META_DATA_12042019.txt\n" +
            "FILE_TYPE                       :         csv\n" +
            "LIST_OF_CUBES                   :         N/A";

    String expectedParsedFile = "{\"VERSION\":\"2.0\",\"DELIMITER\":\"\\u0027|\\u0027\",\"ENCODING_USED\":\"ASCII\",\"CHARACTER_SET_USED\":\"UTF8\",\"NEW_LINE_FEED_CHAR\":\"\\u0027\\\\r\\\\n\\u0027\",\"NUM_OF_DIMENSIONS\":\"4\",\"RECORD_ID\":\"Hydration\",\"PROJECT_NAME\":\"RF_20997_175COKE_20191410\",\"COUNTRY\":\"CHINA\",\"DATE_TIME(WITH TIMEZONE)EXPORT\":\"20191204 11\",\"DATA_CENTRE_LOCATION\":\"RF\",\"ARCHIVE_NAME\":\"Hydration_arch_12042019.ZIP\",\"ARCHIVE_SIZE\":\"289.037 MB\",\"NUM_OF_FILES\":\"7\",\"FILE_NAMES\":[\"Hydration_MKT_12042019.csv\",\"Hydration_PROD_12042019.csv\",\"Hydration_FCT_12042019.csv\",\"Hydration_PER_12042019.csv\",\"Hydration_FACT_DATA_12042019.csv\",\"Hydration_DIM_12042019.csv\",\"Hydration_META_DATA_12042019.txt\"],\"FILE_TYPE\":\"csv\",\"LIST_OF_CUBES\":\"N/A\"}";

    CountryMapping[] countryMappings =
            {
                    new CountryMapping("NORWAY", new String[]{"NO", "NOR"}),
                    new CountryMapping("PORTUGAL", new String[]{"PT", "PTG"}),
            };

    RenameMapping[] renameMappings =
            {
                    new RenameMapping("NORTHERN_IRELAND", new String[]{"IX"})
            };


    // When
    String parsedMetaFile = MetaParserService.parser(metaFile, countryMappings, renameMappings);

    // Then
    Assertions.assertNotNull(parsedMetaFile);
    Assertions.assertEquals(expectedParsedFile, parsedMetaFile);

  }

  @Test
  public void parser_succeed_when_semi_colon_delim() {
    // Given
    String metaFile = "################################ METADATA ################################\n" +
            "\n" +
            "VERSION                         :         2.0\n" +
            "DELIMITER                       :         ';'\n" +
            "ENCODING_USED                   :         ASCII\n" +
            "CHARACTER_SET_USED              :         UTF8\n" +
            "NEW_LINE_FEED_CHAR              :         '\\r\\n'\n" +
            "NUM_OF_DIMENSIONS               :         4\n" +
            "\n" +
            "################################ TRANSACTION DETAILS ################################\n" +
            "\n" +
            "RECORD_ID                       :         Hydration\n" +
            "PROJECT_NAME                    :         RF_20997_175COKE_20191410\n" +
            "COUNTRY                         :         CHINA\n" +
            "CATEGORY                        :         Functional Drink ; \n" +
            "LAST_PERIOD                     :         MONTH - OCT19 ; \n" +
            "DATE_TIME(WITH TIMEZONE)EXPORT  :         20191204 11:48:19 China Standard Time\n" +
            "DATA_CENTRE_LOCATION            :         RF\n" +
            "ARCHIVE_NAME                    :         Hydration_arch_12042019.ZIP\n" +
            "ARCHIVE_SIZE                    :         289.037 MB\n" +
            "NUM_OF_FILES                    :         7\n" +
            "FILE_NAMES                      :         Hydration_MKT_12042019.csv; Hydration_PROD_12042019.csv; Hydration_FCT_12042019.csv; Hydration_PER_12042019.csv; Hydration_FACT_DATA_12042019.csv; Hydration_DIM_12042019.csv; Hydration_META_DATA_12042019.txt\n" +
            "FILE_TYPE                       :         csv\n" +
            "LIST_OF_CUBES                   :         N/A";

    String expectedParsedFile = "{\"VERSION\":\"2.0\",\"DELIMITER\":\"\\u0027;\\u0027\",\"ENCODING_USED\":\"ASCII\",\"CHARACTER_SET_USED\":\"UTF8\",\"NEW_LINE_FEED_CHAR\":\"\\u0027\\\\r\\\\n\\u0027\",\"NUM_OF_DIMENSIONS\":\"4\",\"RECORD_ID\":\"Hydration\",\"PROJECT_NAME\":\"RF_20997_175COKE_20191410\",\"COUNTRY\":\"CHINA\",\"DATE_TIME(WITH TIMEZONE)EXPORT\":\"20191204 11\",\"DATA_CENTRE_LOCATION\":\"RF\",\"ARCHIVE_NAME\":\"Hydration_arch_12042019.ZIP\",\"ARCHIVE_SIZE\":\"289.037 MB\",\"NUM_OF_FILES\":\"7\",\"FILE_NAMES\":[\"Hydration_MKT_12042019.csv\",\"Hydration_PROD_12042019.csv\",\"Hydration_FCT_12042019.csv\",\"Hydration_PER_12042019.csv\",\"Hydration_FACT_DATA_12042019.csv\",\"Hydration_DIM_12042019.csv\",\"Hydration_META_DATA_12042019.txt\"],\"FILE_TYPE\":\"csv\",\"LIST_OF_CUBES\":\"N/A\"}";
    CountryMapping[] countryMappings =
            {
                    new CountryMapping("NORWAY", new String[]{"NO", "NOR"}),
                    new CountryMapping("PORTUGAL", new String[]{"PT", "PTG"}),
            };

    RenameMapping[] renameMappings =
            {
                    new RenameMapping("NORTHERN_IRELAND", new String[]{"IX"})
            };

    // When
    String parsedMetaFile = MetaParserService.parser(metaFile, countryMappings, renameMappings);

    // Then
    Assertions.assertNotNull(parsedMetaFile);
    Assertions.assertEquals(expectedParsedFile, parsedMetaFile);

  }

  @Test
  public void parser_succeed_when_country_name_has_whitespace() {
    // Given
    String metaFile = "################################ METADATA ################################\n" +
            "\n" +
            "VERSION                         :         2.0\n" +
            "DELIMITER                       :         ';'\n" +
            "ENCODING_USED                   :         ASCII\n" +
            "CHARACTER_SET_USED              :         UTF8\n" +
            "NEW_LINE_FEED_CHAR              :         '\\r\\n'\n" +
            "NUM_OF_DIMENSIONS               :         4\n" +
            "\n" +
            "################################ TRANSACTION DETAILS ################################\n" +
            "\n" +
            "RECORD_ID                       :         Hydration\n" +
            "PROJECT_NAME                    :         RF_20997_175COKE_20191410\n" +
            "COUNTRY                         :         _SOUTh1 AFRICA_\n" +
            "CATEGORY                        :         Functional Drink ; \n" +
            "LAST_PERIOD                     :         MONTH - OCT19 ; \n" +
            "DATE_TIME(WITH TIMEZONE)EXPORT  :         20191204 11:48:19 China Standard Time\n" +
            "DATA_CENTRE_LOCATION            :         RF\n" +
            "ARCHIVE_NAME                    :         Hydration_arch_12042019.ZIP\n" +
            "ARCHIVE_SIZE                    :         289.037 MB\n" +
            "NUM_OF_FILES                    :         7\n" +
            "FILE_NAMES                      :         Hydration_MKT_12042019.csv; Hydration_PROD_12042019.csv; Hydration_FCT_12042019.csv; Hydration_PER_12042019.csv; Hydration_FACT_DATA_12042019.csv; Hydration_DIM_12042019.csv; Hydration_META_DATA_12042019.txt\n" +
            "FILE_TYPE                       :         csv\n" +
            "LIST_OF_CUBES                   :         N/A";

    String expectedParsedFile = "{\"VERSION\":\"2.0\",\"DELIMITER\":\"\\u0027;\\u0027\",\"ENCODING_USED\":\"ASCII\",\"CHARACTER_SET_USED\":\"UTF8\",\"NEW_LINE_FEED_CHAR\":\"\\u0027\\\\r\\\\n\\u0027\",\"NUM_OF_DIMENSIONS\":\"4\",\"RECORD_ID\":\"Hydration\",\"PROJECT_NAME\":\"RF_20997_175COKE_20191410\",\"COUNTRY\":\"SOUTH_AFRICA\",\"DATE_TIME(WITH TIMEZONE)EXPORT\":\"20191204 11\",\"DATA_CENTRE_LOCATION\":\"RF\",\"ARCHIVE_NAME\":\"Hydration_arch_12042019.ZIP\",\"ARCHIVE_SIZE\":\"289.037 MB\",\"NUM_OF_FILES\":\"7\",\"FILE_NAMES\":[\"Hydration_MKT_12042019.csv\",\"Hydration_PROD_12042019.csv\",\"Hydration_FCT_12042019.csv\",\"Hydration_PER_12042019.csv\",\"Hydration_FACT_DATA_12042019.csv\",\"Hydration_DIM_12042019.csv\",\"Hydration_META_DATA_12042019.txt\"],\"FILE_TYPE\":\"csv\",\"LIST_OF_CUBES\":\"N/A\"}";
    CountryMapping[] countryMappings =
            {
                    new CountryMapping("NORWAY", new String[]{"NO", "NOR"}),
                    new CountryMapping("PORTUGAL", new String[]{"PT", "PTG"}),
            };
    RenameMapping[] renameMappings =
            {
                    new RenameMapping("NORTHERN_IRELAND", new String[]{"IX"})
            };
    // When
    String parsedMetaFile = MetaParserService.parser(metaFile, countryMappings, renameMappings);

    // Then
    Assertions.assertNotNull(parsedMetaFile);
    Assertions.assertEquals(expectedParsedFile, parsedMetaFile);

  }

  @Test
  public void parser_succeed_when_country_name_empty() {
    // Given
    String metaFile = "################################ METADATA ################################\n" +
            "\n" +
            "VERSION                         :         2.0\n" +
            "DELIMITER                       :         ';'\n" +
            "ENCODING_USED                   :         ASCII\n" +
            "CHARACTER_SET_USED              :         UTF8\n" +
            "NEW_LINE_FEED_CHAR              :         '\\r\\n'\n" +
            "NUM_OF_DIMENSIONS               :         4\n" +
            "\n" +
            "################################ TRANSACTION DETAILS ################################\n" +
            "\n" +
            "RECORD_ID                       :         Hydration\n" +
            "PROJECT_NAME                    :         GTC_TCCC_NARTD_NO_CIP\n" +
            "COUNTRY                         :         \n" +
            "CATEGORY                        :         Functional Drink ; \n" +
            "LAST_PERIOD                     :         MONTH - OCT19 ; \n" +
            "DATE_TIME(WITH TIMEZONE)EXPORT  :         20191204 11:48:19 China Standard Time\n" +
            "DATA_CENTRE_LOCATION            :         RF\n" +
            "ARCHIVE_NAME                    :         nispstd_p12590_t223803090_dCCEP_NARTD_NO_116_arch_09042020.zip\n" +
            "ARCHIVE_SIZE                    :         289.037 MB\n" +
            "NUM_OF_FILES                    :         7\n" +
            "FILE_NAMES                      :         Hydration_MKT_12042019.csv; Hydration_PROD_12042019.csv; Hydration_FCT_12042019.csv; Hydration_PER_12042019.csv; Hydration_FACT_DATA_12042019.csv; Hydration_DIM_12042019.csv; Hydration_META_DATA_12042019.txt\n" +
            "FILE_TYPE                       :         csv\n" +
            "LIST_OF_CUBES                   :         N/A";

    String expectedParsedFile = "{\"VERSION\":\"2.0\",\"DELIMITER\":\"\\u0027;\\u0027\",\"ENCODING_USED\":\"ASCII\",\"CHARACTER_SET_USED\":\"UTF8\",\"NEW_LINE_FEED_CHAR\":\"\\u0027\\\\r\\\\n\\u0027\",\"NUM_OF_DIMENSIONS\":\"4\",\"RECORD_ID\":\"Hydration\",\"PROJECT_NAME\":\"GTC_TCCC_NARTD_NO_CIP\",\"COUNTRY\":\"NORWAY\",\"DATE_TIME(WITH TIMEZONE)EXPORT\":\"20191204 11\",\"DATA_CENTRE_LOCATION\":\"RF\",\"ARCHIVE_NAME\":\"nispstd_p12590_t223803090_dCCEP_NARTD_NO_116_arch_09042020.zip\",\"ARCHIVE_SIZE\":\"289.037 MB\",\"NUM_OF_FILES\":\"7\",\"FILE_NAMES\":[\"Hydration_MKT_12042019.csv\",\"Hydration_PROD_12042019.csv\",\"Hydration_FCT_12042019.csv\",\"Hydration_PER_12042019.csv\",\"Hydration_FACT_DATA_12042019.csv\",\"Hydration_DIM_12042019.csv\",\"Hydration_META_DATA_12042019.txt\"],\"FILE_TYPE\":\"csv\",\"LIST_OF_CUBES\":\"N/A\"}";
    CountryMapping[] countryMappings =
            {
                    new CountryMapping("NORWAY", new String[]{"NO", "NOR"}),
                    new CountryMapping("PORTUGAL", new String[]{"PT", "PTG"}),
            };
    RenameMapping[] renameMappings =
            {
                    new RenameMapping("NORTHERN_IRELAND", new String[]{"IX"})
            };
    // When
    String parsedMetaFile = MetaParserService.parser(metaFile, countryMappings, renameMappings);

    // Then
    Assertions.assertNotNull(parsedMetaFile);
    Assertions.assertEquals(expectedParsedFile, parsedMetaFile);

  }

  @Test
  public void parser_exception_when_country_name_empty_and_no_mapping() {
    // Given
    String metaFile = "################################ METADATA ################################\n" +
            "\n" +
            "VERSION                         :         2.0\n" +
            "DELIMITER                       :         ';'\n" +
            "ENCODING_USED                   :         ASCII\n" +
            "CHARACTER_SET_USED              :         UTF8\n" +
            "NEW_LINE_FEED_CHAR              :         '\\r\\n'\n" +
            "NUM_OF_DIMENSIONS               :         4\n" +
            "\n" +
            "################################ TRANSACTION DETAILS ################################\n" +
            "\n" +
            "RECORD_ID                       :         Hydration\n" +
            "PROJECT_NAME                    :         GTC_TCCC_NARTD_NO_CIP\n" +
            "COUNTRY                         :         \n" +
            "CATEGORY                        :         Functional Drink ; \n" +
            "LAST_PERIOD                     :         MONTH - OCT19 ; \n" +
            "DATE_TIME(WITH TIMEZONE)EXPORT  :         20191204 11:48:19 China Standard Time\n" +
            "DATA_CENTRE_LOCATION            :         RF\n" +
            "ARCHIVE_NAME                    :         nispstd_p12590_t223803090_dCCEP_NARTD_NO_116_arch_09042020.zip\n" +
            "ARCHIVE_SIZE                    :         289.037 MB\n" +
            "NUM_OF_FILES                    :         7\n" +
            "FILE_NAMES                      :         Hydration_MKT_12042019.csv; Hydration_PROD_12042019.csv; Hydration_FCT_12042019.csv; Hydration_PER_12042019.csv; Hydration_FACT_DATA_12042019.csv; Hydration_DIM_12042019.csv; Hydration_META_DATA_12042019.txt\n" +
            "FILE_TYPE                       :         csv\n" +
            "LIST_OF_CUBES                   :         N/A";

    CountryMapping[] countryMappings =
            {
                    new CountryMapping("NORWAY", new String[]{"NOP", "NOR"}),
                    new CountryMapping("PORTUGAL", new String[]{"PT", "PTG"}),
            };
    RenameMapping[] renameMappings =
            {
                    new RenameMapping("NORTHERN_IRELAND", new String[]{"IX"})
            };
    // When
    Throwable throwable = Assertions.assertThrows(Throwable.class, () -> {
      MetaParserService.parser(metaFile, countryMappings, renameMappings);
    });

    Assertions.assertTrue(throwable.getMessage().contains("Country name is empty in metafile."));

  }

  @Test
  public void parser_succeed_when_country_name_empty_and_country_code_has_3_chars() {
    // Given
    String metaFile = "################################ METADATA ################################\n" +
            "\n" +
            "VERSION                         :         2.0\n" +
            "DELIMITER                       :         ';'\n" +
            "ENCODING_USED                   :         ASCII\n" +
            "CHARACTER_SET_USED              :         UTF8\n" +
            "NEW_LINE_FEED_CHAR              :         '\\r\\n'\n" +
            "NUM_OF_DIMENSIONS               :         4\n" +
            "\n" +
            "################################ TRANSACTION DETAILS ################################\n" +
            "\n" +
            "RECORD_ID                       :         Hydration\n" +
            "PROJECT_NAME                    :         GTC_TCCC_NARTD_NOr_CIP\n" +
            "COUNTRY                         :         \n" +
            "CATEGORY                        :         Functional Drink ; \n" +
            "LAST_PERIOD                     :         MONTH - OCT19 ; \n" +
            "DATE_TIME(WITH TIMEZONE)EXPORT  :         20191204 11:48:19 China Standard Time\n" +
            "DATA_CENTRE_LOCATION            :         RF\n" +
            "ARCHIVE_NAME                    :         Hydration_arch_12042019.ZIP\n" +
            "ARCHIVE_SIZE                    :         289.037 MB\n" +
            "NUM_OF_FILES                    :         7\n" +
            "FILE_NAMES                      :         Hydration_MKT_12042019.csv; Hydration_PROD_12042019.csv; Hydration_FCT_12042019.csv; Hydration_PER_12042019.csv; Hydration_FACT_DATA_12042019.csv; Hydration_DIM_12042019.csv; Hydration_META_DATA_12042019.txt\n" +
            "FILE_TYPE                       :         csv\n" +
            "LIST_OF_CUBES                   :         N/A";

    String expectedParsedFile = "{\"VERSION\":\"2.0\",\"DELIMITER\":\"\\u0027;\\u0027\",\"ENCODING_USED\":\"ASCII\",\"CHARACTER_SET_USED\":\"UTF8\",\"NEW_LINE_FEED_CHAR\":\"\\u0027\\\\r\\\\n\\u0027\",\"NUM_OF_DIMENSIONS\":\"4\",\"RECORD_ID\":\"Hydration\",\"PROJECT_NAME\":\"GTC_TCCC_NARTD_NOr_CIP\",\"COUNTRY\":\"NORWAY\",\"DATE_TIME(WITH TIMEZONE)EXPORT\":\"20191204 11\",\"DATA_CENTRE_LOCATION\":\"RF\",\"ARCHIVE_NAME\":\"Hydration_arch_12042019.ZIP\",\"ARCHIVE_SIZE\":\"289.037 MB\",\"NUM_OF_FILES\":\"7\",\"FILE_NAMES\":[\"Hydration_MKT_12042019.csv\",\"Hydration_PROD_12042019.csv\",\"Hydration_FCT_12042019.csv\",\"Hydration_PER_12042019.csv\",\"Hydration_FACT_DATA_12042019.csv\",\"Hydration_DIM_12042019.csv\",\"Hydration_META_DATA_12042019.txt\"],\"FILE_TYPE\":\"csv\",\"LIST_OF_CUBES\":\"N/A\"}";
    CountryMapping[] countryMappings =
            {
                    new CountryMapping("NORWAY", new String[]{"NO", "NOR"}),
                    new CountryMapping("PORTUGAL", new String[]{"PT", "PTG"}),
            };
    RenameMapping[] renameMappings =
            {
                    new RenameMapping("NORTHERN_IRELAND", new String[]{"IX"})
            };
    // When
    String parsedMetaFile = MetaParserService.parser(metaFile, countryMappings, renameMappings);

    // Then
    Assertions.assertNotNull(parsedMetaFile);
    Assertions.assertEquals(expectedParsedFile, parsedMetaFile);

  }

  @Test
  public void parser_succeed_when_there_is_rename_mapping() {
    // Given
    String metaFile = "################################ METADATA ################################\n" +
            "\n" +
            "VERSION                         :         2.0\n" +
            "DELIMITER                       :         ';'\n" +
            "ENCODING_USED                   :         ASCII\n" +
            "CHARACTER_SET_USED              :         UTF8\n" +
            "NEW_LINE_FEED_CHAR              :         '\\r\\n'\n" +
            "NUM_OF_DIMENSIONS               :         4\n" +
            "\n" +
            "################################ TRANSACTION DETAILS ################################\n" +
            "\n" +
            "RECORD_ID                       :         Hydration\n" +
            "PROJECT_NAME                    :         nispstd_p10945_t522783690_dTCCC_Juice_IX_96\n" +
            "COUNTRY                         :         IRELAND\n" +
            "CATEGORY                        :         Functional Drink ; \n" +
            "LAST_PERIOD                     :         MONTH - OCT19 ; \n" +
            "DATE_TIME(WITH TIMEZONE)EXPORT  :         20191204 11:48:19 China Standard Time\n" +
            "DATA_CENTRE_LOCATION            :         RF\n" +
            "ARCHIVE_NAME                    :         nispstd_p10945_t522783690_dTCCC_Juice_IX_96.ZIP\n" +
            "ARCHIVE_SIZE                    :         289.037 MB\n" +
            "NUM_OF_FILES                    :         7\n" +
            "FILE_NAMES                      :         Hydration_MKT_12042019.csv; Hydration_PROD_12042019.csv; Hydration_FCT_12042019.csv; Hydration_PER_12042019.csv; Hydration_FACT_DATA_12042019.csv; Hydration_DIM_12042019.csv; Hydration_META_DATA_12042019.txt\n" +
            "FILE_TYPE                       :         csv\n" +
            "LIST_OF_CUBES                   :         N/A";

    String expectedParsedFile = "{\"VERSION\":\"2.0\",\"DELIMITER\":\"\\u0027;\\u0027\",\"ENCODING_USED\":\"ASCII\",\"CHARACTER_SET_USED\":\"UTF8\",\"NEW_LINE_FEED_CHAR\":\"\\u0027\\\\r\\\\n\\u0027\",\"NUM_OF_DIMENSIONS\":\"4\",\"RECORD_ID\":\"Hydration\",\"PROJECT_NAME\":\"nispstd_p10945_t522783690_dTCCC_Juice_IX_96\",\"COUNTRY\":\"NORTHERN_IRELAND\",\"DATE_TIME(WITH TIMEZONE)EXPORT\":\"20191204 11\",\"DATA_CENTRE_LOCATION\":\"RF\",\"ARCHIVE_NAME\":\"nispstd_p10945_t522783690_dTCCC_Juice_IX_96.ZIP\",\"ARCHIVE_SIZE\":\"289.037 MB\",\"NUM_OF_FILES\":\"7\",\"FILE_NAMES\":[\"Hydration_MKT_12042019.csv\",\"Hydration_PROD_12042019.csv\",\"Hydration_FCT_12042019.csv\",\"Hydration_PER_12042019.csv\",\"Hydration_FACT_DATA_12042019.csv\",\"Hydration_DIM_12042019.csv\",\"Hydration_META_DATA_12042019.txt\"],\"FILE_TYPE\":\"csv\",\"LIST_OF_CUBES\":\"N/A\"}";
    CountryMapping[] countryMappings =
            {
                    new CountryMapping("NORWAY", new String[]{"NO", "NOR"}),
                    new CountryMapping("PORTUGAL", new String[]{"PT", "PTG"}),
            };
    RenameMapping[] renameMappings =
            {
                    new RenameMapping("NORTHERN_IRELAND", new String[]{"IX"})
            };
    // When
    String parsedMetaFile = MetaParserService.parser(metaFile, countryMappings, renameMappings);

    // Then
    Assertions.assertNotNull(parsedMetaFile);
    Assertions.assertEquals(expectedParsedFile, parsedMetaFile);

  }

  @Test
  public void parser_succeed_when_there_is_rename_mapping_and_name_mapping_and_country_name_not_empty() {
    // Given
    String metaFile = "################################ METADATA ################################\n" +
            "\n" +
            "VERSION                         :         2.0\n" +
            "DELIMITER                       :         ';'\n" +
            "ENCODING_USED                   :         ASCII\n" +
            "CHARACTER_SET_USED              :         UTF8\n" +
            "NEW_LINE_FEED_CHAR              :         '\\r\\n'\n" +
            "NUM_OF_DIMENSIONS               :         4\n" +
            "\n" +
            "################################ TRANSACTION DETAILS ################################\n" +
            "\n" +
            "RECORD_ID                       :         Hydration\n" +
            "PROJECT_NAME                    :         TCCC JBB D1011ZZ7 SFF\n" +
            "COUNTRY                         :         SPAIN\n" +
            "CATEGORY                        :         Functional Drink ; \n" +
            "LAST_PERIOD                     :         MONTH - OCT19 ; \n" +
            "DATE_TIME(WITH TIMEZONE)EXPORT  :         20191204 11:48:19 China Standard Time\n" +
            "DATA_CENTRE_LOCATION            :         RF\n" +
            "ARCHIVE_NAME                    :         TCCC_Juice2_ES_arch_02162022.zip\n" +
            "ARCHIVE_SIZE                    :         289.037 MB\n" +
            "NUM_OF_FILES                    :         7\n" +
            "FILE_NAMES                      :         Hydration_MKT_12042019.csv; Hydration_PROD_12042019.csv; Hydration_FCT_12042019.csv; Hydration_PER_12042019.csv; Hydration_FACT_DATA_12042019.csv; Hydration_DIM_12042019.csv; Hydration_META_DATA_12042019.txt\n" +
            "FILE_TYPE                       :         csv\n" +
            "LIST_OF_CUBES                   :         N/A";

    String expectedParsedFile = "{\"VERSION\":\"2.0\",\"DELIMITER\":\"\\u0027;\\u0027\",\"ENCODING_USED\":\"ASCII\",\"CHARACTER_SET_USED\":\"UTF8\",\"NEW_LINE_FEED_CHAR\":\"\\u0027\\\\r\\\\n\\u0027\",\"NUM_OF_DIMENSIONS\":\"4\",\"RECORD_ID\":\"Hydration\",\"PROJECT_NAME\":\"TCCC JBB D1011ZZ7 SFF\",\"COUNTRY\":\"SPAIN_SINCAT\",\"DATE_TIME(WITH TIMEZONE)EXPORT\":\"20191204 11\",\"DATA_CENTRE_LOCATION\":\"RF\",\"ARCHIVE_NAME\":\"TCCC_Juice2_ES_arch_02162022.zip\",\"ARCHIVE_SIZE\":\"289.037 MB\",\"NUM_OF_FILES\":\"7\",\"FILE_NAMES\":[\"Hydration_MKT_12042019.csv\",\"Hydration_PROD_12042019.csv\",\"Hydration_FCT_12042019.csv\",\"Hydration_PER_12042019.csv\",\"Hydration_FACT_DATA_12042019.csv\",\"Hydration_DIM_12042019.csv\",\"Hydration_META_DATA_12042019.txt\"],\"FILE_TYPE\":\"csv\",\"LIST_OF_CUBES\":\"N/A\"}";
    CountryMapping[] countryMappings =
            {
                    new CountryMapping("NORWAY", new String[]{"NO", "NOR"}),
                    new CountryMapping("PORTUGAL", new String[]{"PT", "PTG"}),
                    new CountryMapping("SPAIN", new String[]{"ES", "ESP"}),
            };
    RenameMapping[] renameMappings =
            {
                    new RenameMapping("NORTHERN_IRELAND", new String[]{"IX"}),
                    new RenameMapping("SPAIN_SINCAT", new String[]{"CSD-NCBS1_ES",
                            "CSD-NCBS2_ES",
                            "CSD-NCBS3_ES",
                            "CSD-NCBS_ES",
                            "Juice1_ES",
                            "Juice2_ES",
                            "Juice3_ES",
                            "Juice_ES",
                            "RTDAlcohol_ES",
                            "Water1_ES",
                            "Water2_ES",
                            "Water_ES",
                            "ES"
                    })
            };
    // When
    String parsedMetaFile = MetaParserService.parser(metaFile, countryMappings, renameMappings);

    // Then
    Assertions.assertNotNull(parsedMetaFile);
    Assertions.assertEquals(expectedParsedFile, parsedMetaFile);

  }


  @Test
  public void parser_succeed_when_country_name_empty_and_renaming_config_exists() {
    // Given
    String metaFile = "################################ METADATA ################################\n" +
            "\n" +
            "VERSION                         :         2.0\n" +
            "DELIMITER                       :         ';'\n" +
            "ENCODING_USED                   :         ASCII\n" +
            "CHARACTER_SET_USED              :         UTF8\n" +
            "NEW_LINE_FEED_CHAR              :         '\\r\\n'\n" +
            "NUM_OF_DIMENSIONS               :         4\n" +
            "\n" +
            "################################ TRANSACTION DETAILS ################################\n" +
            "\n" +
            "RECORD_ID                       :         Hydration\n" +
            "PROJECT_NAME                    :         GTC_TCCC_NARTD_ES\n" +
            "COUNTRY                         :         \n" +
            "CATEGORY                        :         Functional Drink ; \n" +
            "LAST_PERIOD                     :         MONTH - OCT19 ; \n" +
            "DATE_TIME(WITH TIMEZONE)EXPORT  :         20191204 11:48:19 China Standard Time\n" +
            "DATA_CENTRE_LOCATION            :         RF\n" +
            "ARCHIVE_NAME                    :         Hydration_arch_12042019.ZIP\n" +
            "ARCHIVE_SIZE                    :         289.037 MB\n" +
            "NUM_OF_FILES                    :         7\n" +
            "FILE_NAMES                      :         Hydration_MKT_12042019.csv; Hydration_PROD_12042019.csv; Hydration_FCT_12042019.csv; Hydration_PER_12042019.csv; Hydration_FACT_DATA_12042019.csv; Hydration_DIM_12042019.csv; Hydration_META_DATA_12042019.txt\n" +
            "FILE_TYPE                       :         csv\n" +
            "LIST_OF_CUBES                   :         N/A";

    String expectedParsedFile = "{\"VERSION\":\"2.0\",\"DELIMITER\":\"\\u0027;\\u0027\",\"ENCODING_USED\":\"ASCII\",\"CHARACTER_SET_USED\":\"UTF8\",\"NEW_LINE_FEED_CHAR\":\"\\u0027\\\\r\\\\n\\u0027\",\"NUM_OF_DIMENSIONS\":\"4\",\"RECORD_ID\":\"Hydration\",\"PROJECT_NAME\":\"GTC_TCCC_NARTD_ES\",\"COUNTRY\":\"SPAIN\",\"DATE_TIME(WITH TIMEZONE)EXPORT\":\"20191204 11\",\"DATA_CENTRE_LOCATION\":\"RF\",\"ARCHIVE_NAME\":\"Hydration_arch_12042019.ZIP\",\"ARCHIVE_SIZE\":\"289.037 MB\",\"NUM_OF_FILES\":\"7\",\"FILE_NAMES\":[\"Hydration_MKT_12042019.csv\",\"Hydration_PROD_12042019.csv\",\"Hydration_FCT_12042019.csv\",\"Hydration_PER_12042019.csv\",\"Hydration_FACT_DATA_12042019.csv\",\"Hydration_DIM_12042019.csv\",\"Hydration_META_DATA_12042019.txt\"],\"FILE_TYPE\":\"csv\",\"LIST_OF_CUBES\":\"N/A\"}";
    CountryMapping[] countryMappings =
            {
                    new CountryMapping("NORWAY", new String[]{"NO", "NOR"}),
                    new CountryMapping("PORTUGAL", new String[]{"PT", "PTG"}),
                    new CountryMapping("SPAIN", new String[]{"ES", "ESP"}),


            };
    RenameMapping[] renameMappings =
            {
                    new RenameMapping("NORTHERN_IRELAND", new String[]{"IX"}),
                    new RenameMapping("SPAIN_SINCAT", new String[]{
                            "CSD-NCBS1_ES",
                            "CSD-NCBS2_ES",
                            "CSD-NCBS3_ES",
                            "CSD-NCBS_ES",
                            "Juice1_ES",
                            "Juice2_ES",
                            "Juice3_ES",
                            "Juice_ES",
                            "RTDAlcohol_ES",
                            "Water1_ES",
                            "Water2_ES",
                            "Water_ES",
                    })
            };
    // When
    String parsedMetaFile = MetaParserService.parser(metaFile, countryMappings, renameMappings);

    // Then
    Assertions.assertNotNull(parsedMetaFile);
    Assertions.assertEquals(expectedParsedFile, parsedMetaFile);

  }

//  @Test(expected = NullPointerException.class)
//  public void parser_null_value() {
//    // Given
//    String metaFile = null;
//    CountryMapping[] countryMappings =
//            {
//                    new CountryMapping("NORWAY", new String[]{"NO", "NOR"}),
//                    new CountryMapping("PORTUGAL", new String[]{"PT", "PTG"}),
//            };
//
//    // When
//    String parsedMetaFile = MetaParserService.parser(metaFile, countryMappings);
//  }
}



