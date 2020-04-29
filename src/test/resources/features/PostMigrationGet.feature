Feature: Post-Migration-Get

  @Smoke
  @Priority:2
  @dataFile:src/main/resources/file.xls
  @sheetName:${serviceName}
  Scenario: Response code is correct

    Given   '${Source URL}' is not null
    When    '${Source URL}' is hit
    Then    Source ResponseCode is equal to '${Response Code}'

   @Regression
   @Priority:1
   @dataFile:src/main/resources/file.xls
   @sheetName:${serviceName}
   Scenario: Response Body is correct

     Given   '${Endpoint Name}' is not null
     And     '${Migrated API Endpoint}' is not null
     When    '${Endpoint Name}' benchmark body is retrieved
     And     '${Migrated API Endpoint}' is being hitted
     And      BenchMark vs Migrated Endpoint Comparison is performed
