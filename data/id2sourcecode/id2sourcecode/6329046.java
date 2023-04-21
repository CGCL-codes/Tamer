    public static QuotaDetails retrieveAppQuotaDetails(String userid, String password, String source, String application) throws Exception {
        String authCookie = LogonHelper.loginToGoogleAppEngine(userid, password, source);
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            HttpGet get = new HttpGet("https://appengine.google.com/dashboard/quotadetails?&app_id=" + application);
            get.setHeader("Cookie", "ACSID=" + authCookie);
            HttpResponse response = client.execute(get);
            return new QuotaDetailsParser().parse(response.getEntity().getContent());
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
