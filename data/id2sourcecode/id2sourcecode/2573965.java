    public static String getType(final URL url) throws IOException {
        if (url == null) {
            return null;
        }
        URLConnection con = url.openConnection();
        return con.getContentType();
    }
