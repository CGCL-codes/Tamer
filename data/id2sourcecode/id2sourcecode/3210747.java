    private String[] handlePOSTRequest(SequenceModel statement, final boolean loadExistingCookies) throws Exception {
        String responseTuple[] = { "", "", "" };
        String str_url = statement.getRequestUrl();
        URL url = LoadTestManager.getSSLURL(str_url);
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        LoadTestManager.postDataSSL(statement.getPostData(), conn, url, str_url, false, loadExistingCookies);
        System.out.println("done posting SSL data");
        return responseTuple;
    }
