    public static InputStream getUrlStream(String urlToRetrieve) throws IOException {
        URL url = new URL(urlToRetrieve);
        URLConnection conn = url.openConnection();
        InputStream in = conn.getInputStream();
        String encoding = conn.getContentEncoding();
        if (encoding != null) {
            if (encoding.equals("gzip")) in = new GZIPInputStream(in);
        }
        return in;
    }
