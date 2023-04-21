    private static String simulateWebLoginAndGetOauthVerifier(LinkedInRequestToken requestToken) {
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Username"), TestConstants.LINKED_IN_TEST_USER_NAME);
        assertNotNullOrEmpty(String.format(RESOURCE_MISSING_MESSAGE, "Password"), TestConstants.LINKED_IN_TEST_PASSWORD);
        try {
            Map<String, String> parametersMap = getParametersMap(requestToken, TestConstants.LINKED_IN_TEST_USER_NAME, TestConstants.LINKED_IN_TEST_PASSWORD);
            URL url = new URL(requestToken.getAuthorizationUrl());
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();
            if (request.getResponseCode() != 200) {
                fail(convertStreamToString(request.getErrorStream()));
            }
            String tokenPage = convertStreamToString(request.getInputStream());
            Matcher m = CSRF_TOKEN_PATTERN.matcher(tokenPage);
            if (m.matches()) {
                parametersMap.put("csrfToken", "ajax:" + m.group(2));
            }
            url = new URL(LOGIN_URL);
            request = (HttpURLConnection) url.openConnection();
            request.setDoOutput(true);
            request.setRequestMethod("POST");
            PrintStream out = new PrintStream(new BufferedOutputStream(request.getOutputStream()));
            out.print(getParametersString(parametersMap));
            out.flush();
            out.close();
            request.connect();
            if (request.getResponseCode() != 200) {
                fail(convertStreamToString(request.getErrorStream()));
            }
            String verifierPage = convertStreamToString(request.getInputStream());
            m = OAUTH_VERIFIER_PATTERN.matcher(verifierPage);
            if (m.matches()) {
                return m.group(2);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return null;
    }
