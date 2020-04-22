Feature: Post-Migration-Get

  @Smoke
  @Priority:1
  @dataFile:src/main/resources/file.xls
  @sheetName:${serviceName}
  Scenario: Response code is correct

    Given   '${Source URL}' is not null
    When    '${Source URL}' is hit
    Then    Source ResponseCode is equal to '${Response Code}'
