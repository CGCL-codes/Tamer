    public static String loginToGoogleAppEngine(String userid, String password, String source) throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("accountType", "HOSTED_OR_GOOGLE"));
            nvps.add(new BasicNameValuePair("Email", userid));
            nvps.add(new BasicNameValuePair("Passwd", password));
            nvps.add(new BasicNameValuePair("service", "ah"));
            nvps.add(new BasicNameValuePair("source", source));
            HttpPost post = new HttpPost("https://www.google.com/accounts/ClientLogin");
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new Exception("Error obtaining ACSID");
            }
            String authToken = getAuthToken(response.getEntity().getContent());
            post.abort();
            HttpGet get = new HttpGet("https://appengine.google.com/_ah/login?auth=" + authToken);
            response = client.execute(get);
            for (Cookie cookie : client.getCookieStore().getCookies()) {
                if (cookie.getName().startsWith("ACSID")) {
                    return cookie.getValue();
                }
            }
            get.abort();
            throw new Exception("Did not find ACSID cookie");
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
