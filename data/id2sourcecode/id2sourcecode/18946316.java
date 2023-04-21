    private static String facebookGetMethod(String url) {
        System.out.println("@executing facebookGetMethod():" + url);
        String responseStr = null;
        try {
            HttpGet loginGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(loginGet);
            HttpEntity entity = response.getEntity();
            System.out.println("facebookGetMethod: " + response.getStatusLine());
            if (entity != null) {
                responseStr = EntityUtils.toString(entity);
                entity.consumeContent();
            }
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                System.out.println("Error Occured! Status Code = " + statusCode);
                responseStr = null;
            }
            System.out.println("Get Method done(" + statusCode + "), response string length: " + (responseStr == null ? 0 : responseStr.length()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return responseStr;
    }
