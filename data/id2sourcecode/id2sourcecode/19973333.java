    public static void justVisitPage(URL url, HttpClient httpclient) throws IOException {
        HttpGet httpget = new HttpGet(url.toExternalForm());
        HttpResponse response = httpclient.execute(httpget);
        response.getEntity().consumeContent();
    }
