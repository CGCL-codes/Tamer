    private void executePOSTRequest(String url, String input) {
        DefaultHttpClient httpClient = null;
        try {
            httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(url);
            StringEntity inputEntity = new StringEntity(input);
            inputEntity.setContentType("application/json");
            postRequest.setEntity(inputEntity);
            HttpResponse response = httpClient.execute(postRequest);
            if (response.getStatusLine().getStatusCode() != Response.Status.CREATED.getStatusCode()) {
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
