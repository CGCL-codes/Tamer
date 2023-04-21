    public static byte[] readURLToByteArray(URL url) throws IOException {
        return readInputStreamToByteArray(url.openConnection().getInputStream());
    }
