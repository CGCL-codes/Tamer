    public static void main(String[] args) throws Exception {
        String authCookie = LogonHelper.loginToGoogleAppEngine("alois.belaska@gmail.com", "", "eShops-WatchDog");
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            HttpGet get = new HttpGet("https://appengine.google.com/dashboard/quotadetails?&app_id=eshopsengine");
            get.setHeader("Cookie", "ACSID=" + authCookie);
            HttpResponse response = client.execute(get);
            LineNumberReader reader = new LineNumberReader(new BufferedReader(new InputStreamReader(response.getEntity().getContent())));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
