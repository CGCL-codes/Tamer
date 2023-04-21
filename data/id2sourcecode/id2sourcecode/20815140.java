    public static InputStream openInputStream(String uri) throws IOException {
        InputStream in = null;
        if (uri.startsWith("http://") || uri.startsWith("https://") || uri.startsWith("file://") || uri.startsWith("ftp://")) {
            URL url = new URL(uri);
            in = url.openStream();
        } else {
            in = new FileInputStream(uri);
        }
        if (uri.toLowerCase().endsWith(".gz")) {
            return new GZIPInputStream(in);
        }
        return in;
    }
