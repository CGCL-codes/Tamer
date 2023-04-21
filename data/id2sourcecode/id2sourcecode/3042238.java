    private static InputStream httpGet(Uri uri) throws IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(uri.toString());
        HttpResponse response = client.execute(request);
        StatusLine status = response.getStatusLine();
        if (status.getStatusCode() != 200) throw new IOException("HTTP error " + status);
        return response.getEntity().getContent();
    }
