    public static void dowloadResource(WebView view, String url, File file) throws IOException {
        String cookie = CookieManager.getInstance().getCookie(url);
        MyHttpClient client = new MyHttpClient();
        HttpGet request = new HttpGet(url);
        request.setHeader("User-Agent", view.getSettings().getUserAgentString());
        request.setHeader("Cookie", cookie);
        HttpResponse response = client.execute(request);
        FileOutputStream fos = new FileOutputStream(file);
        HttpEntity ent = response.getEntity();
        ent.writeTo(fos);
        ent.consumeContent();
        request.abort();
        fos.close();
    }
