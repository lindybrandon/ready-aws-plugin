package com.smartbear.aws;

public final class Strings {
    private static final String REFERENCE_TO_ISSUES = " You can search and create issues for this plugin at https://github.com/SmartBear/ready-aws-plugin/issues.";
    private Strings() {
    }

    public static final class PluginInfo {
        public static final String NAME = "AWS API Gateway Plugin";
        public static final String DESCRIPTION = "Adds actions for importing APIs from Amazon Web Service API Gateway";
    }

    public static final class AddApiAction {
        public static final String NAME = "Add API from AWS API Gateway";
        public static final String DESCRIPTION = "Imports APIs from an Amazon Web Service API Gateway";
    }

    //TODO:
    public static final class Error {
        public static final String INVALID_KEY = "";
        public static final String NO_SUCH_ALGORITHM = "";
        public static final String UNSUPPORTED_ENCODING = "";
        public static final String MALFORMED_URL = "";
        public static final String UNABLE_CREATE_CONNECTION = "";
        public static final String UNAVAILABLE_HOST = "The \"%s\" host is unavailable or invalid.";
        public static final String UNAVAILABLE_DATA = "No data available at the \"%s\" location." + REFERENCE_TO_ISSUES;
        public static final String INVALID_JSON_RESPONSE = "Can't parse JSON of the response of the request to the \"%s\" location." + REFERENCE_TO_ISSUES;
        public static final String UNEXPECTED_RESPONSE_FORMAT = "Unexpected response format of the JSON for the %s." + REFERENCE_TO_ISSUES;
    }

    //TODO:
    public static final class AccountInfoDialog {
        public static final String PROMPT_API_DIALOG_CAPTION = "Import APIs from AWS API Gateway";
        public static final String PROMPT_API_DIALOG_DESCRIPTION = "Enter an account details";

        public static final String ACCESS_KEY_LABEL = "Access Key";
        public static final String ACCESS_KEY_DESCRIPTION = "Access Key";
        public static final String SECRET_KEY_LABEL = "Secret Key";
        public static final String SECRET_KEY_DESCRIPTION = "Secret Key";
        public static final String REGION_LABEL = "Region";
        public static final String REGION_DESCRIPTION = "Region";

        public static final String EMPTY_FIELD_WARNING = "Please enter '%s'";
    }

    public static final class SelectApiDialog {
        public static final String CAPTION = "Select APIs to Import";
        public static final String DESCRIPTION = "Please select APIs you would like to import into the project from the list below";
        public static final String NAME_LABEL = "API Name";
        public static final String DESCRIPTION_LABEL = "API Description";
        public static final String DEFINITION_LABEL = "API Definition";
        public static final String GEN_TEST_SUITE = "Generate Test Suite";
        public static final String GEN_LOAD_TEST = "Generate Load Test";
        public static final String GEN_SECUR_TEST = "Generate Security Test";
        public static final String GEN_VIRT_HOST = "Generate Virtual Host";
        public static final String NOTHING_SELECTED_WARNING = "Please select at least one API to import";
    }
}
