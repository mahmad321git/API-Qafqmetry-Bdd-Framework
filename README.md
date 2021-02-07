# API-Qafqmetry-Bdd-Framework

❗️ 𝗣𝗿𝗼𝗯𝗹𝗲𝗺 - 𝗦𝘁𝗮𝘁𝗲𝗺𝗲𝗻𝘁: There are several # of backend API's that are built and linked with the on-premise database and have been migrated onto the cloud. These are being built using the micro-services architecture. As an Automation Enginner you have to verify that API'S are successfully migrated.

🔪 𝗣𝗿𝗼𝗯𝗹𝗲𝗺 - 𝗦𝗼𝗹𝘂𝘁𝗶𝗼𝗻: So in-order to solve this problem first we have to identify how many services that needs to be tested? and with in each service how many API's that are being built? and with-in each API how many different end points it has?

<p>Lets take an Considerate Assumption:</br>
- # of Services that needs to be tested: 10</br>
- # of API's that Exist with in the service: 10</br>
- # of End points with in an API: 10</br></p>

📐 𝗜𝗺𝗽𝗼𝗿𝘁𝗮𝗻𝘁 𝗙𝗿𝗮𝗺𝗲𝘄𝗼𝗿𝗸 & 𝗟𝗶𝗯𝗿𝗮𝗿𝗶𝗲𝘀 𝘂𝘀𝗲𝗱:
- RestAssured
- TestNg
- BDD (Gherkins- Cucumber)

📜 𝗙𝗼𝗿 𝗿𝗲𝗽𝗼𝗿𝘁𝗶𝗻𝗴 𝘄𝗲 𝗵𝗮𝘃𝗲 𝘂𝘀𝗲𝗱:
- QAF

👉 𝗦𝗶𝗹𝗲𝗻𝘁 𝗙𝗲𝗮𝘁𝘂𝗿𝗲𝘀 𝗼𝗳 𝗔𝘂𝘁𝗼𝗺𝗮𝘁𝗶𝗼𝗻 𝗙𝗿𝗮𝗺𝗲𝘄𝗼𝗿𝗸:
- RESTful API Support</br>
- Configurable API endpoints from a separate config file</br>
- Cross-Environment execution feasibility</br>
- Parallel test case execution feasibility</br>
- Retry Mechanism in case of failure (Configurable)</br>
- Test Suite Management (Configurable)</br>
- API vs API comparison (JSON Payloads & Responses)</br>
  - Response Code Validation (Source vs Target)</br>
  - Response Body [Data+Attributes] Validation (Source vs Target)</br>
- A concise html test result report yielded as Extent Report</br>
- Test Results email as an attachment to desired audience via AWS SES (Simple Email Service)</br>
