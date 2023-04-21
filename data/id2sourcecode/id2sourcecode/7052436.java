    public static byte[] dowloadResource(WebView view, String url) throws IOException {
        String cookie = CookieManager.getInstance().getCookie(url);
        MyHttpClient client = new MyHttpClient();
        HttpGet request = new HttpGet(url);
        request.setHeader("User-Agent", view.getSettings().getUserAgentString());
        request.setHeader("Cookie", cookie);
        HttpResponse response = client.execute(request);
        TmpByteArrayOutputStream fos = new TmpByteArrayOutputStream(2048);
        HttpEntity ent = response.getEntity();
        ent.writeTo(fos);
        ent.consumeContent();
        request.abort();
        fos.close();
        return fos.toByteArray();
    }
