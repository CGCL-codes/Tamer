    private static String loadWebsiteHtmlCode(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet getMethod = new HttpGet(url);
        String htmlCode = "";
        if (header != null) {
            getMethod.setHeader(header);
        }
        try {
            HttpResponse resp = httpClient.execute(getMethod);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                System.out.println("Method failed!" + statusCode);
            }
            htmlCode = EntityUtils.toString(resp.getEntity());
            header = resp.getHeaders("Set-Cookie")[0];
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
        }
        return htmlCode;
    }
