    public InputStream getDownloadURLInputStream() throws Exception {
        URL url = new URL(tracker.getTrackingDownloadFile());
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        connection.setConnectTimeout(this.urlConnectionTimeout);
        return connection.getInputStream();
    }
