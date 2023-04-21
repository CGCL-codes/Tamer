    private String getAuthCookie(String authToken) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            httpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
            String uri = Setup.PROD_URL + "/_ah/login?continue=http://localhost/&auth=" + authToken;
            HttpGet method = new HttpGet(uri);
            HttpResponse res = httpClient.execute(method);
            StatusLine statusLine = res.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            Header[] headers = res.getHeaders("Set-Cookie");
            if (statusCode != 302 || headers.length == 0) {
                return null;
            }
            for (Cookie cookie : httpClient.getCookieStore().getCookies()) {
                if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
                    return AUTH_COOKIE_NAME + "=" + cookie.getValue();
                }
            }
        } catch (IOException e) {
            Log.w(TAG, "Got IOException " + e);
            Log.w(TAG, Log.getStackTraceString(e));
        } finally {
            httpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
        }
        return null;
    }
