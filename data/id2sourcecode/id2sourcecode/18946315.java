    private static String facebookPostMethod(String host, String urlPostfix, List<NameValuePair> nvps) {
        System.out.println("@executing facebookPostMethod():" + host + urlPostfix);
        String responseStr = null;
        try {
            HttpPost httpost = new HttpPost(host + urlPostfix);
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse postResponse = httpClient.execute(httpost);
            HttpEntity entity = postResponse.getEntity();
            System.out.println("facebookPostMethod: " + postResponse.getStatusLine());
            if (entity != null) {
                responseStr = EntityUtils.toString(entity);
                entity.consumeContent();
            }
            System.out.println("Post Method done(" + postResponse.getStatusLine().getStatusCode() + "), response string length: " + (responseStr == null ? 0 : responseStr.length()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return responseStr;
    }
