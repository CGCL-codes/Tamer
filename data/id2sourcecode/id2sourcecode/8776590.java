    private static void update4(String uri, List<BasicNameValuePair> params) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPut put = new HttpPut(uri);
        put.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        try {
            HttpResponse response = httpClient.execute(put);
            System.out.println("response statusLine: " + response.getStatusLine());
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
