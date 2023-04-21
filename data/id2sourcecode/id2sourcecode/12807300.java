    private void executeGETRequest(String url, String mimeType) {
        DefaultHttpClient httpClient = null;
        try {
            httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(url);
            getRequest.addHeader("accept", mimeType);
            HttpResponse response = httpClient.execute(getRequest);
            if (response.getStatusLine().getStatusCode() != Response.Status.OK.getStatusCode()) {
                logger.error("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
                Assert.fail();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            String output;
            logger.info("Response for URL: " + url);
            while ((output = br.readLine()) != null) {
                logger.info(output);
            }
            logger.info("\n");
        } catch (ClientProtocolException e) {
            logger.error("An exception has occurred", e);
            Assert.fail();
        } catch (IOException e) {
            logger.error("An exception has occurred", e);
            Assert.fail();
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
    }
