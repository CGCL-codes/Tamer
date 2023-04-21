    private InputStream doSearch(String keyword) {
        String urlStr = Config.getBaseUrl();
        InputStream is = null;
        urlStr += keyword;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) is = connection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }
