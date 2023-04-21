    private void executeDELETERequest(String url) {
        DefaultHttpClient httpClient = null;
        try {
            httpClient = new DefaultHttpClient();
            HttpDelete deleteRequest = new HttpDelete(url);
            HttpResponse response = httpClient.execute(deleteRequest);
            if (response.getStatusLine().getStatusCode() != Response.Status.OK.getStatusCode() && response.getStatusLine().getStatusCode() != Response.Status.NO_CONTENT.getStatusCode()) {
                logger.error("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
                Assert.fail();
            }
            if (response.getStatusLine().getStatusCode() == Response.Status.OK.getStatusCode()) {
                BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
                String output;
                logger.info("Response for URL: " + url);
                while ((output = br.readLine()) != null) {
                    logger.info(output);
                }
                logger.info("\n");
            }
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
