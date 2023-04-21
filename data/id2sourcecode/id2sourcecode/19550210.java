    public InputStream fetch(String url, Boolean use_mobile_header) {
        HttpClient client = new DefaultHttpClient();
        URI uri;
        try {
            uri = new URI(url);
            HttpGet method = new HttpGet(uri);
            if (use_mobile_header) {
                method.setHeader("User-Agent", "Mozilla/5.0 (Linux; U; Android 0.5; en-us) AppleWebKit/522+ (KHTML, like Gecko) Safari/419.3");
            } else {
                method.setHeader("User-Agent", "Mozilla/5.0 (Linux; U; en-us) AppleWebKit/522+ (KHTML, like Gecko) Safari/419.3");
            }
            HttpResponse res = client.execute(method);
            Log.v("BB", res.getStatusLine().toString());
            Header location_headers[] = res.getHeaders("Content-Location");
            if (location_headers.length == 1) {
                final_host = new URL(location_headers[0].getValue()).getHost();
            }
            HttpEntity e = res.getEntity();
            Log.v("BB", Long.toString(e.getContentLength()));
            Log.v("BB", e.getContentType().toString());
            return e.getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
