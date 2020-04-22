package com.qmetry.qaf.example.stepdefinitions;

import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.step.QAFTestStepProvider;
import com.qmetry.qaf.automation.util.Reporter;
import com.qmetry.qaf.automation.util.Validator;
import com.qmetry.qaf.example.utils.FileManager;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;

/**
 * @author rehman.ashraf
 */
@QAFTestStepProvider()
public class BaseApiSteps {

    private final Headers headers = new Headers();
    public static Response endpointResponse;
    public static List<Map<Object, Object>> responseBodyJson = new ArrayList<>();
    public static List<List<Map<Object, Object>>> responseBodyList = new ArrayList<>();
    public static List<Object> responseCode = new ArrayList<>();
    public static String CreatedRecord;

    @QAFTestStep(description = "{0} is not null")
    public void isNotNull(String endpointUrl) {
        if (endpointUrl != null) {
            Reporter.log("Endpoint: " + endpointUrl + " is not null");
        } else
            Assert.fail();
    }

    @QAFTestStep(description = "{endpoint} is hit")
    public void urlIsHit(String endpoint) {
        endpointResponse = RestAssured.given().urlEncodingEnabled(false).headers(headers).get(endpoint);
        Reporter.log(endpoint+" is hit");
        responseCode.add(endpointResponse.getStatusCode());
        if ((endpointResponse.jsonPath().get()) instanceof HashMap) {
            responseBodyJson.add(endpointResponse.jsonPath().getMap("$"));
            Reporter.log(endpoint+" response is stored");
        }
        else if ((endpointResponse.jsonPath().get()) instanceof ArrayList) {
            responseBodyList.add(endpointResponse.jsonPath().getList("$"));
            Reporter.log(endpoint+" response is stored");
        }
        else {
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
}