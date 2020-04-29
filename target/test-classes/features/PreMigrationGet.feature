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


  @Smoke
  @Priority:1
  @dataFile:src/main/resources/file.xls
  @sheetName:${serviceName}
  Scenario: Attribute Count is correct

    Given  '${DIT URL}' is not null
    And    '${JAVA URL}' is not null
    When   '${DIT URL}' is hit
    And    '${JAVA URL}' is hit
    Then   Source Attribute Count is equal to Target Attribute Count


