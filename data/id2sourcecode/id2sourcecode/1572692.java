    public static Report load(URL url) throws IOException {
        InputStream in = url.openStream();
        try {
            return load(in);
        } finally {
            in.close();
        }
    }
