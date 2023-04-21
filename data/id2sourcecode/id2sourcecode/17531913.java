    private boolean isImageEmpty(String url) {
        try {
            HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
            return httpConn.getContentLength() == 807;
        } catch (IOException e) {
            return true;
        }
    }
