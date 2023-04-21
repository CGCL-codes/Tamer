    private static boolean ping() throws Exception {
        boolean ret = false;
        final DefaultHttpClient httpClient = new DefaultHttpClient();
        final HttpGet request = new HttpGet("http://localhost:8063/static/bug4j.css");
        final HttpResponse httpResponse = httpClient.execute(request);
        final StatusLine statusLine = httpResponse.getStatusLine();
        final int statusCode = statusLine.getStatusCode();
        if (statusCode == 200) {
            ret = true;
        }
        return ret;
    }
