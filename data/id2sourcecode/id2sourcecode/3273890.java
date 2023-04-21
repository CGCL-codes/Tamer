    public Request(String url, int connectTimeout, int readTimeout) {
        try {
            this.url = url;
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);
        } catch (Exception e) {
            throw new HttpException("Failed URL: " + url, e);
        }
    }
