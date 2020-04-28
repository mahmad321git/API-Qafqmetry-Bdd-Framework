Feature: Pre-Migration-Post

  @Smoke
  @Priority:3
  @dataFile:src/main/resources/file.xls
  @sheetName:qaf
  @key:Update
  Scenario: Response is updated and validated
    Given  '${POST URL}' is not null
    And    '${Payload}' is not null
    And    '${GET URL}' is not null
    When   '${POST URL}' is hit with '${Payload}' to update the record
    And    '${GET URL}' is hit
    Then    Response of '${GET URL}' is equal to '${Payload}'

  @Smoke
  @Priority:5
  @dataFile:src/main/resources/file.xls
  @sheetName:qaf
  @key:Create
  Scenario: Response is created and validated

    Given  '${POST URL}' is not null
    And    '${Payload}' is not null
    And    '${GET URL}' is not null
    When   '${POST URL}' is hit with '${Payload}' to update the record
    And    '${GET URL}' is hit with newly created record
    Then    Response of '${GET URL}' is equal to '${Payload}'

