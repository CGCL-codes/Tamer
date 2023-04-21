    public URLBlob(URL url, int timeout) throws IOException {
        connection = url.openConnection();
        connection.setReadTimeout(timeout);
    }
