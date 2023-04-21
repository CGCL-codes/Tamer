    public static InputStream openInputStream(URL url) throws IOException {
        return url.openConnection().getInputStream();
    }
