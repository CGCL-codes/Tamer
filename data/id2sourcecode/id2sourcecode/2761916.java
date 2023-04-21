    public static BufferedImage read(URL url) throws IOException {
        if (url == null) throw new IOException("null url");
        InputStream in = null;
        try {
            in = url.openStream();
            if (in == null) throw new IOException("Can't open connection to " + url.toExternalForm());
            return read(new BufferedInputStream(in));
        } finally {
            if (in != null) in.close();
        }
    }
