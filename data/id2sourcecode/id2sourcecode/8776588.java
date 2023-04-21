    public static void delete(String uri, Map params) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpDelete del = new HttpDelete(uri);
        HttpResponse response = httpClient.execute(del);
        System.out.println(response.getStatusLine());
    }
