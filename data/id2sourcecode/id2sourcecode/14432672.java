    protected InputStream loadUrl(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.connect();
        DataInputStream remoteObj = new DataInputStream(connection.getInputStream());
        return remoteObj;
    }
