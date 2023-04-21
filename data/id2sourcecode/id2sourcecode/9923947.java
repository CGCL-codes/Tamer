    private static void sendDeleteRequest(String endpoint, NodeCookie cookie) throws Exception {
        URL url = new URL(endpoint);
        cookie.addCookieToUrl(url);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpCon.setRequestMethod("DELETE");
        httpCon.connect();
        cookie.storeCookie();
    }
