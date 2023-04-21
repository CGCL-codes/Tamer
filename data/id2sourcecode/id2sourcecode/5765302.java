    private static String getStringContentFromURL(URL url) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        InputStream inputStream = url.openStream();
        int len;
        byte[] b = new byte[128];
        while ((len = inputStream.read(b)) != -1) result.write(b, 0, len);
        inputStream.close();
        return result.toString();
    }
