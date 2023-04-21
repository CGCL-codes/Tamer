    public static InputStream inputStream(URL url) throws IOException {
        URLConnection uc = openConnection(url);
        return uc.getInputStream();
    }
