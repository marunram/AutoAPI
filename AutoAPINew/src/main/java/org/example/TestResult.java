package org.example;

import java.util.ArrayList;
import java.util.List;

public class TestResult {

    int statusCode;
    boolean status;
    List<TestStep> testStepList = new ArrayList<>();

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<TestStep> getTestStepList() {
        return testStepList;
    }

    public void setTestStepList(List<TestStep> testStepList) {
        this.testStepList = testStepList;
    }

    public void addTestStepList(TestStep testStep) {
        this.testStepList.add(testStep);
    }

    @Override
    public String toString() {
        StringBuilder testSteps = new StringBuilder();
        for (int i = 0; i < testStepList.size(); i++) {
            TestStep testStep = testStepList.get(i);
            testSteps.append("Step#" + (i + 1) + "=> ");
            if (testStep.isStatus()) {
                testSteps.append("PASS: ");
            } else {
                testSteps.append("FAIL:");
            }
            testSteps.append(testStep.getTestCommand());
            testSteps.append("\n");
        }


        if (this.status) {
            return "PASS" + "\n" + "Status code=" + this.statusCode + "\n" + testSteps.toString();
        } else {
            return "FAIL" + "\n" + "Status code=" + this.statusCode + "\n" + testSteps.toString();
        }

    }
}
