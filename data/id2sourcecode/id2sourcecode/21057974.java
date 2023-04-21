    public static String get(String url) {
        HttpGet method = new HttpGet(url);
        try {
            HttpResponse response = client.execute(method);
            int returnCode = response.getStatusLine().getStatusCode();
            if ((returnCode >= 200) && (returnCode < 300)) {
                return method.getResponseBodyAsString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
