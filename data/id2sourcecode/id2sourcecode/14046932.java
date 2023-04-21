    private void makeTestApiRequest(LinkedInAccessToken accessToken) {
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "API URL"), TestConstants.LINKED_IN_TEST_API_URL);
        try {
            URL url = new URL(TestConstants.LINKED_IN_TEST_API_URL);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            service.signRequestWithToken(request, accessToken);
            request.connect();
            if (request.getResponseCode() != 200) {
                fail(convertStreamToString(request.getErrorStream()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
