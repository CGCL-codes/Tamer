    public static void sendUrl(String url) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet client = new HttpGet(url);
        try {
            HttpResponse response = httpclient.execute(client);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
