    public static byte[] getBytes(URL url) throws IOException {
        return getBytes(url.openConnection().getInputStream());
    }
