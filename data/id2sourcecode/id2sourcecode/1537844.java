    public void initializeConnection() throws MalformedURLException, IOException {
        URL urlLocal = new URL(this.getUrl());
        connection = (HttpURLConnection) urlLocal.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod(this.getMethod());
        if (this.getTimeout() <= 0) {
            this.setTimeout(50000);
        }
        connection.setConnectTimeout(this.getTimeout());
        if (c_type != null) {
            connection.addRequestProperty("Content-Type", c_type);
        }
    }
