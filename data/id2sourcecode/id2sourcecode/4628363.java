    public static boolean checkValidHtmlPage(String page) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        URI url = null;
        try {
            url = new URI(page);
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
        HttpGet httpget = new HttpGet(url);
        try {
            HttpResponse response = httpclient.execute(httpget);
            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            } else {
                return false;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
