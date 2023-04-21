    public static byte[] getImageDataFromUrl(String url) throws IOException {
        return getByteArrayFromStream(new BufferedInputStream(new URL(url).openStream()));
    }
