    public static final HttpURLConnection connect(String url) {
        URL urlObj = null;
        HttpURLConnection urlCon = null;
        try {
            urlObj = new URL(url);
            urlCon = (HttpURLConnection) urlObj.openConnection();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlCon;
    }
