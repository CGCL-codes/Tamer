    public static int getSize(final URL url) throws IOException {
        if (url == null) {
            return -1;
        }
        URLConnection con = url.openConnection();
        return con.getContentLength();
    }
