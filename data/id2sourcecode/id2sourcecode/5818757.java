    private void loadTestCaseConfig() {
        Properties testCases = new Properties();
        try {
            final URL url = configService.getConfigFileUrl(getTestCaseConfigFile());
            final InputStream inputStream = url.openStream();
            log.info("loading test case config from" + url.toExternalForm());
            testCases.loadFromXML(inputStream);
        } catch (IOException e) {
            log.error("cannot load test case config from file: " + getTestCaseConfigFile(), e);
        }
        praiseResponse = extractTestCase("praise", testCases);
        scoldingResponse = extractTestCase("scolding", testCases);
        defaultResponseHeader = extractTestCase("defaultResponseHeader", testCases);
        defaultResponseContent = extractTestCase("defaultResponseContent", testCases);
        defaultResponseFooter = extractTestCase("defaultResponseFooter", testCases);
        textResponses.clear();
        for (Map.Entry<Object, Object> entry : testCases.entrySet()) {
            textResponses.put(normalize((String) entry.getKey()), stripWhiteSpaceFromString((String) entry.getValue()));
        }
    }
