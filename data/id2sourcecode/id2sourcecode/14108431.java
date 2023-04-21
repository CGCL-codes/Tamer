    public static String get(URL url) throws IOException {
        if (url == null) {
            throw new IllegalArgumentException("URI cannot be null.");
        }
        InputStream is = url.openStream();
        DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
        return getString(dis);
    }
