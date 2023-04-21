    private void blockingAuthenticateWithToken(Account account, String authToken) throws AuthenticationException, InvalidAuthTokenException {
        HttpGet httpGet = new HttpGet(String.format(mAuthUrlTemplate, authToken));
        String acsidToken = null;
        try {
            HttpResponse response = mHttpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 403) {
                throw new InvalidAuthTokenException();
            }
            List<Cookie> cookies = mHttpClient.getCookieStore().getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("ACSID")) {
                    acsidToken = cookie.getValue();
                    break;
                }
            }
            if (acsidToken == null && response.getStatusLine().getStatusCode() == 500) {
                throw new InvalidAuthTokenException("ACSID cookie not found in HTTP response: " + response.getStatusLine().toString() + "; assuming invalid auth token.");
            }
            mTokenStoreHelper.putToken(account, acsidToken);
        } catch (ClientProtocolException e) {
            throw new AuthenticationException("HTTP Protocol error authenticating to App Engine", e);
        } catch (IOException e) {
            throw new AuthenticationException("IOException authenticating to App Engine", e);
        }
    }
