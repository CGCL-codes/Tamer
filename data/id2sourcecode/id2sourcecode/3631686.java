    public String getMimeType() throws Exception {
        if (url == null) throw new Exception("Cannot get Mime type from undefined URL.");
        URLConnection connection = url.openConnection();
        return (connection.getContentType());
    }
