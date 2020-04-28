package com.qmetry.qaf.example.stepdefinitions;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.step.QAFTestStepProvider;
import com.qmetry.qaf.automation.util.Reporter;
import com.qmetry.qaf.automation.util.Validator;
import com.qmetry.qaf.example.utils.FileManager;
import com.qmetry.qaf.example.utils.FileUtils;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import org.testng.Assert;

import lombok.extern.slf4j.Slf4j;
import org.testng.util.Strings;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * @author rehman.ashraf
 */
@Slf4j
@QAFTestStepProvider()
public class BaseApiSteps {

    private final Headers headers = new Headers();
    public static Response endpointResponse;
    public static List<Map<Object, Object>> responseBodyJson = new ArrayList<>();
    public static List<List<Map<Object, Object>>> responseBodyList = new ArrayList<>();
    public static List<Object> responseCode = new ArrayList<>();
    public static String CreatedRecord;

    public static String benchMarkStructure;
    //BenchMarkApi
    public static List<Map> BenchMarkResponseList = null;
    public static Map<Object, Object> benchMarkJson = null;

    //Variables for RestAssured Responses
    public static Response migratedResponse = null;
    public static Response originalResponse = null;

    //MigratedApi
    public static List<Map> migratedResponseList = null;
    public static Map<Object,Object> migratedJson = null;

    @QAFTestStep(description = "{0} is not null")
    public void isNotNull(String endpointUrl) {
        if (endpointUrl != null) {
            Reporter.log("Endpoint: " + endpointUrl + " is not null");
        } else
            Assert.fail();
    }

    @QAFTestStep(description = "{endpoint} is hit")
    public void urlIsHit(String endpoint) {
            endpointResponse = RestAssured.given().urlEncodingEnabled(true).headers(headers).get(endpoint);
            Reporter.log(endpoint + " is hit");
            responseCode.add(endpointResponse.getStatusCode());
            if ((endpointResponse.jsonPath().get()) instanceof HashMap) {
                responseBodyJson.add(endpointResponse.jsonPath().getMap("$"));
                Reporter.log(endpoint + " response is stored");
            } else if ((endpointResponse.jsonPath().get()) instanceof ArrayList) {
                responseBodyList.add(endpointResponse.jsonPath().getList("$"));
                Reporter.log(endpoint + " response is stored");
            } else {
                Reporter.log("Invalid Response Type");
            }
    }

    @QAFTestStep(description = "Source ResponseCode is equal to Target ResponseCode")
    public void sourceResponseCodeIsEqualToTargetResponseCode() {
        Reporter.log("Response Codes are: "+ responseCode);
        Assert.assertEquals(responseCode.get(0), responseCode.get(1));
    }

    @QAFTestStep(description = "Source ResponseBody is equal to Target ResponseBody")
    public void sourceResponseBodyIsEqualToTargetResponseBody() {
        if ((endpointResponse.jsonPath().get()) instanceof HashMap) {
            Reporter.log("Response Bodies is: " + responseBodyJson);
            Assert.assertEquals(responseBodyJson.get(0), responseBodyJson.get(1));
            Reporter.log("BOTH RESPONSE BODIES ARE SAME");
        }
        else if ((endpointResponse.jsonPath().get()) instanceof ArrayList) {
            Reporter.log("Response Bodies is: " + responseBodyList);
            Assert.assertEquals(responseBodyList.get(0), responseBodyList.get(1));
            Reporter.log("BOTH RESPONSE BODIES ARE SAME");
        }
        else {
            Reporter.log("Invalid Response Type");
        }
    }


    @QAFTestStep(description = "Source Attribute Count is equal to Target Attribute Count")
    public void sourceCountIsEqualToTargetCount() {
        if ((endpointResponse.jsonPath().get()) instanceof HashMap) {
            Reporter.log("Source endpoint count"+responseBodyJson.get(0).size());
            Reporter.log("Target endpoint count"+responseBodyJson.get(1).size());
            Assert.assertEquals(responseBodyJson.get(0).size(), responseBodyJson.get(1).size());

            Reporter.log("BOTH ATTRIBUTE COUNT ARE SAME");
        }
        else if ((endpointResponse.jsonPath().get()) instanceof ArrayList) {
            Reporter.log("Source endpoint count"+responseBodyList.get(0).size());
            Reporter.log("Target endpoint count"+responseBodyList  .get(1).size());
            Assert.assertEquals(responseBodyList.get(0).size(), responseBodyList.get(1).size());
            Reporter.log("BOTH ATTRIBUTE COUNT ARE SAME");
        }
        else {
            Reporter.log("Invalid Response Type");
        }
    }

    @QAFTestStep(description = "Source ResponseCode is equal to {0}")
    public void sourceResponseCodeShouldBe(Integer response_code) {
        Reporter.log("Response Codes are: "+ responseCode);
        Assert.assertEquals(responseCode.get(0), response_code);
        Reporter.log("Response Code Matches");
    }



    @QAFTestStep(description = "{POST URL} is hit with {Payload} to update the record")
    public void postUrlIsHitWithPayloadToUpdateTheRecord(String postUrl, String payload) {
        Response postRequest = RestAssured.given().urlEncodingEnabled(false).headers("content-type", "application/json").body(payload).post(postUrl);
        Reporter.log(postUrl + " is hit");
        Reporter.log("Status Code: " + postRequest.getStatusCode());
        Reporter.log("Response: " + postRequest.prettyPrint());
        Validator.givenThat(postRequest.getStatusCode(), equalTo(200));
        CreatedRecord = postRequest.path("id").toString();
    }

    @QAFTestStep(description = "Response of {GET URL} is equal to {Payload}")
    public void responseOfGetUrlIsEqualToPayload(String getUrl, String payload) {

        Map<Object, Object> postRequestJson = FileManager.stringToMap(payload);

        if(!postRequestJson.containsKey("id")){
            responseBodyJson.get(0).remove("id");
            Validator.verifyThat(responseBodyJson.get(0), equalTo(postRequestJson));
        }else {
            Validator.verifyThat(responseBodyJson.get(0), equalTo(postRequestJson));
        }
    }

    @QAFTestStep(description = "{GET URL} is hit with newly created record")
    public void getUrlIsHitWithNewlyCreatedRecord(String endpoint) {

        Response endpointResponse = RestAssured.given().urlEncodingEnabled(false).headers(headers).get(endpoint.concat(CreatedRecord));
        Validator.verifyThat(endpointResponse.getStatusCode(), equalTo(200));
        Reporter.log(endpoint.concat(CreatedRecord) + " is hit with Status Code" + endpointResponse.getStatusCode());
        responseCode.add(endpointResponse.getStatusCode());
        responseBodyJson.add(endpointResponse.jsonPath().getMap("$"));
    }

    @QAFTestStep(description = "{Endpoint Name} is ! null")
    public void endPointNameIsNotNull(String endpointName) {
        if (endpointName != null) {
            Reporter.log("Endpoint: " + endpointName + " is not null");
        } else
            Assert.fail();
    }

    @QAFTestStep(description = "{Migrated API Endpoint} is not empty")
    public void migratedAPIURLIsNotNull(String migratedAPIURL) {
        if (migratedAPIURL != null) {
            Reporter.log("Endpoint: " + migratedAPIURL + " is not null");
        } else
            Assert.fail();
    }

    @QAFTestStep(description = "{Endpoint Name} body is retrieved")
    public void benchMarkBodyRetrieval(String benchMarkEndPointName) {
        File file = FileUtils.getFileFromResources("benchmarkresponse/" + benchMarkEndPointName + ".json");

        ObjectMapper mapper = new ObjectMapper();

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            benchMarkStructure = FileUtils.returnFile(file);

            if (benchMarkStructure.charAt(0) == '[') {
                // convert JSON string to List of Map
                BenchMarkResponseList = Arrays.asList(mapper.readValue(benchMarkStructure, Map[].class));
            }
            if (benchMarkStructure.charAt(0) == '{') {
                // convert JSON string to Map
                benchMarkJson = new TreeMap<>(mapper.readValue(benchMarkStructure, Map.class));
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    @QAFTestStep(description = "{Migrated API EndPoint} is being hitted")
    public void migratedEndPointHit(String migratedEndPointHit) {
        originalResponse = RestAssured.given().urlEncodingEnabled(false).headers(headers).get(migratedEndPointHit);
    }

    @QAFTestStep(description = "BenchMark vs Migrated Endpoint Comparison is performed")
    public void benchMarkVsMigratedBodyComparison() {

        if((originalResponse.jsonPath().get()) instanceof HashMap) {
            migratedJson = new TreeMap<>(originalResponse.jsonPath().getMap("$"));
        }
        else if((originalResponse.jsonPath().get()) instanceof ArrayList) {
            migratedResponseList = originalResponse.jsonPath().getList("$");
        } else {
            Reporter.log("SOURCE_INVALID_RESPONSE_MESSAGE");
        }
        try {
            if (migratedJson != null && benchMarkJson != null) {
                Assert.assertEquals(migratedJson, benchMarkJson);
                Reporter.log("EQUAL_RESPONSE_MESSAGE");
            } else if (migratedResponseList != null && BenchMarkResponseList != null) {
                Assert.assertEquals(migratedResponseList, BenchMarkResponseList);
                Reporter.log("EQUAL_RESPONSE_MESSAGE");
            }
        } catch (AssertionError error) {
            Assert.fail();
        }
    }
}