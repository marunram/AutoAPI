package org.example;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.squareup.okhttp.*;

import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        ExcelReader excelReader = new ExcelReader();
        try {
            List<TestCase> testCases = excelReader.readTestCaseFromExcel();
            for (TestCase testCase : testCases) {
//                System.out.println("Test case"+ testCase.toString());

                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");


                if (testCase.getMethod().equalsIgnoreCase("post")) {
                    RequestBody body = RequestBody.create(JSON, testCase.getBody());
                    Request request = new Request.Builder().url(testCase.getUrl()).post(body).build();
                    Response response = client.newCall(request).execute();
//                    System.out.println("My Response" + response.body().string());
                    TestResult testResult = new TestResult();
                    testResult.setStatusCode(response.code());
                    if (testCase.getResponseCode() == response.code()) {
                        testResult.setStatus(true);

                        String responseData = response.body().string();
                        Object document = Configuration.defaultConfiguration().jsonProvider().parse(responseData);
                        String[] arrValidation = testCase.getResponse().split("\n");
                        for (int i = 0; i < arrValidation.length; i++) {
                            TestStep testStep = new TestStep();
                            String strValidation = arrValidation[i];
                            String[] arr = strValidation.split("=");
                            if (arr.length == 2) {
                                String jPath = arr[0];
                                String expectedValue = arr[1];
                                String actualValue = JsonPath.read(document, jPath);
                                if (!expectedValue.equalsIgnoreCase(actualValue)) {
                                    testResult.setStatus(false);
                                    testStep.setStatus(false);
                                    testStep.setTestCommand("JPath=" + jPath + "Expected=" + expectedValue + " Actual=" + actualValue + "\n");
                                } else {
                                    testStep.setStatus(true);
                                    testStep.setTestCommand("JPath=" + jPath + "Expected=" + expectedValue + " Actual=" + actualValue + "\n");
                                }
                                testResult.addTestStepList(testStep);
                            } else {
                                testStep.setStatus(false);
                                testResult.addTestStepList(testStep);
                            }
                        }
                    } else {
                        testResult.setStatus(false);
                    }
                    System.out.println("Test result=" + testResult.toString());
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}