    public URLBlob(URL url) throws IOException {
        connection = url.openConnection();
    }
