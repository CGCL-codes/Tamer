    public ProgressableURLInputStream(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        stream = connection.getInputStream();
        fileSize = connection.getContentLength();
    }
