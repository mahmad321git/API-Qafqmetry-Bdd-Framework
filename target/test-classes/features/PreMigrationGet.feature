Feature: Pre-Migration-Get

  @Smoke
  @Priority:1
  @dataFile:src/main/resources/file.xls
  @sheetName:${serviceName}
  Scenario: Response Code is correct

    Given  '${Source URL}' is not null
    And    '${Target URL}' is not null
    When   '${Source URL}' is hit
    And    '${Target URL}' is hit
    Then    Source ResponseCode is equal to Target ResponseCode


  @Smoke
  @Priority:2
  @dataFile:src/main/resources/file.xls
  @sheetName:${serviceName}
  Scenario: Response Body is correct

    Given   '${Source URL}' is not null
    And     '${Target URL}' is not null
    When    '${Source URL}' is hit
    And     '${Target URL}' is hit
    Then    Source ResponseBody is equal to Target ResponseBody

