    public ScanResult doScan() throws ScanException {
        Timestamp timeOfScan = new Timestamp(System.currentTimeMillis());
        if (!isReady()) return new HttpStaticScanResult(ScanResultCode.UNREADY, timeOfScan);
        HostConfiguration hostConfig = new HostConfiguration();
        int port = 80;
        String protocol = "HTTP";
        if (specimenUrl.getPort() >= 0) port = specimenUrl.getPort();
        if (specimenUrl.getProtocol() != null && specimenUrl.getProtocol() != "") protocol = specimenUrl.getProtocol();
        hostConfig.setHost(specimenUrl.getHost(), port, protocol);
        HttpMethod httpMethod = new GetMethod(specimenUrl.toString());
        httpMethod.setFollowRedirects(followRedirects);
        HttpClient httpClient = new HttpClient();
        try {
            httpClient.executeMethod(hostConfig, httpMethod);
        } catch (HttpException e) {
        } catch (IOException e) {
            HttpStaticScanResult scanResult = new HttpStaticScanResult(ScanResultCode.SCAN_FAILED, timeOfScan);
            scanResult.expectedResponseCode = expectedResponseCode;
            scanResult.expectedDataHash = expectedDataHash;
            scanResult.expectedDataHashAlgorithm = expectedDataHashAlgorithm;
            scanResult.specimenUrl = specimenUrl;
            logScanResult(ScanResultCode.SCAN_FAILED, scanResult.deviations, HttpStaticScanRule.RULE_TYPE, specimenUrl.toString(), "Connection failed");
            return scanResult;
        }
        InputStream httpDataInStream = null;
        boolean inputError = false;
        try {
            httpDataInStream = httpMethod.getResponseBodyAsStream();
        } catch (IOException e) {
            inputError = true;
        }
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(expectedDataHashAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new ScanException("Hash algorithm (" + expectedDataHashAlgorithm + ") was not found", e);
        }
        byte[] inputBytes = new byte[4096];
        int bytesRead = 0;
        if (inputError == false && httpDataInStream != null) {
            try {
                while ((bytesRead = httpDataInStream.read(inputBytes)) > 0) {
                    messageDigest.update(inputBytes, 0, bytesRead);
                }
            } catch (IOException e) {
                inputError = true;
            }
        }
        byte[] hashBytes = messageDigest.digest();
        String observedHashValue = new String(Hex.encodeHex(hashBytes));
        Header[] headers = httpMethod.getResponseHeaders();
        Vector<HttpHeaderScanResult> headerRuleMatches = new Vector<HttpHeaderScanResult>();
        for (int c = 0; c < headers.length; c++) {
            HttpHeaderScanResult ruleResult = analyzeHeader(headers[c].getName(), headers[c].getValue());
            headerRuleMatches.add(ruleResult);
        }
        HttpStaticScanResult scanResult;
        if (inputError) scanResult = new HttpStaticScanResult(ScanResultCode.SCAN_FAILED, timeOfScan); else scanResult = new HttpStaticScanResult(ScanResultCode.SCAN_COMPLETED, timeOfScan);
        scanResult.expectedResponseCode = expectedResponseCode;
        scanResult.expectedDataHash = expectedDataHash;
        scanResult.expectedDataHashAlgorithm = expectedDataHashAlgorithm;
        scanResult.specimenUrl = specimenUrl;
        scanResult.setActualHash(expectedDataHashAlgorithm, observedHashValue);
        scanResult.setActualResponseCode(httpMethod.getStatusCode());
        scanResult.headerResults = new HttpHeaderScanResult[headerRuleMatches.size()];
        for (int c = 0; c < headerRuleMatches.size(); c++) {
            scanResult.headerResults[c] = headerRuleMatches.get(c);
        }
        logScanResult(ScanResultCode.SCAN_COMPLETED, scanResult.deviations, HttpStaticScanRule.RULE_TYPE, specimenUrl.toString());
        return scanResult;
    }
