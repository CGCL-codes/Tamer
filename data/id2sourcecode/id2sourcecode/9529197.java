    private byte[] loadIconFromURL(String urlString) throws MalformedURLException, IOException {
        byte[] data;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        InputStream input = new BufferedInputStream(connection.getInputStream());
        data = new byte[connection.getContentLength()];
        input.read(data);
        input.close();
        return data;
    }
