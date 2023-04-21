    private boolean loginSyncServer(HttpContext localContext, DefaultHttpClient httpClient, HttpPost httpPost) throws ClientProtocolException, IOException {
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("pmpadmin", "admin");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope("gsssvn", AuthScope.ANY_PORT), creds);
        httpClient.setCredentialsProvider(credsProvider);
        HttpResponse response = httpClient.execute(httpPost, localContext);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return true;
        } else {
            System.out.println("login失敗:" + response.getStatusLine());
            return false;
        }
    }
