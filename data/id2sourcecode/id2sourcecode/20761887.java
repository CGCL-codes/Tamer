    private static String Execute(HttpRequestBase httprequest) throws IOException, ClientProtocolException {
        httprequest.setHeader("Content-type", "application/x-www-form-urlencoded");
        httprequest.setHeader("UserAgent", "cachebox");
        String result = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(httprequest);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        while ((line = rd.readLine()) != null) {
            result += line + "\n";
        }
        return result;
    }
