package com.qmetry.qaf.example.stepdefinitions;

import com.cdk.qa.framework.config.extent.ExtentTestManager;
import com.cdk.qa.framework.utils.Constants;
import com.cdk.qa.framework.utils.FileUtils;
import com.cdk.qa.framework.utils.RestAssuredUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.relevantcodes.extentreports.LogStatus;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 *  Implementation of the test cases for the migrated endpoints
 *
 * @author Ahmad.Idrees
 */
@Slf4j
public class BenchMarkTests extends ApiValidationBaseClass {

    private final String DATA_PROVIDER = "data-provider";
    private final String REGRESSION = "Regression";


    /**
     * Retrieve endpoint configuration from the excel file
     *
     * @return configurations
     */
    @DataProvider(name = DATA_PROVIDER)
    public Object[][] dataProviderMethod() throws IOException {
        String serviceSheetName = System.getProperty("serviceName");
        return FileUtils.getEndpointConfig(serviceSheetName);
    }

    /**
     * Validation of Migrated Endpoint vs Bench Mark Body Comparison
     *
     * @param serviceName, Name of the service for which test case is to be executed
     * @param migratedAPIUrl, URL of the Source API Server
     * //@param BenchMarkStructure, URL of the Migrated API Server
     */
    @SuppressWarnings("unchecked")
    @Test(dataProvider = DATA_PROVIDER, groups = REGRESSION, priority = 1)
    public void benchMarkComparison(String serviceName, String migratedAPIUrl) {

        ExtentTestManager.startTest(serviceName.concat(": BenchMarkTest"), "BenchMarkComparison");

        String benchMarkStructure;
        //MigratedApi
        List<Map> migratedResponseList = null;
        Map<Object,Object> migratedJson = null;

        //BenchMarkApi
        Map<Object, Object> benchMarkJson = null;
        List<Map> BenchMarkResponseList = null;

        File file = FileUtils.getFileFromResources("benchmarkresponse/" + serviceName + ".json");

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

        getSourceResponse(migratedAPIUrl);

        if((originalResponse.jsonPath().get()) instanceof HashMap) {
            migratedJson = new TreeMap<>(originalResponse.jsonPath().getMap("$"));
        }
        else if((originalResponse.jsonPath().get()) instanceof ArrayList) {
            migratedResponseList = originalResponse.jsonPath().getList("$");
        } else {
            ExtentTestManager.getTest().log(LogStatus.INFO, Constants.SOURCE_INVALID_RESPONSE_MESSAGE);
        }
        try {

            if (migratedJson != null && benchMarkJson != null) {

                ExtentTestManager.getTest().log(LogStatus.INFO, "MAP Difference: "
                        + Maps.difference(migratedJson, benchMarkJson));

                Assert.assertEquals(migratedJson, benchMarkJson);

                ExtentTestManager.getTest().log(LogStatus.INFO,
                        FileUtils.getPropertyValue(Constants.MESSAGES_PROPERTIES_PATH,
                                Constants.EQUAL_RESPONSE_MESSAGE));
            } else if (migratedResponseList != null && BenchMarkResponseList != null) {

                Assert.assertEquals(migratedResponseList, BenchMarkResponseList);

                ExtentTestManager.getTest().log(LogStatus.INFO,
                        FileUtils.getPropertyValue(Constants.MESSAGES_PROPERTIES_PATH,
                                Constants.EQUAL_RESPONSE_MESSAGE));
            }
        } catch (AssertionError error) {
            ExtentTestManager.getTest().log(LogStatus.FAIL, error.getMessage());
            Assert.fail();
        }
    }

    /**
     * Executes at the end of the test suite to populate Extent Report and Send it as email
     *
     * @throws MessagingException, Exception thrown in case of message sending failure
     */
    @AfterSuite(groups = {REGRESSION})
    public void endTestSuite() throws MessagingException {
        ExtentTestManager.flush();
        RestAssuredUtils.resetBasePath();
        //emailService.sendEmail();
    }
}
