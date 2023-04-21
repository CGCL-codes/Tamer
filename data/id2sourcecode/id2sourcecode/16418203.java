    private static InputStream openStream(URL sxURL) throws IOException {
        InputStream isx = null;
        if (sxURL != null) {
            URLConnection urlConn = sxURL.openConnection();
            urlConn.setUseCaches(false);
            isx = urlConn.getInputStream();
        }
        return isx;
    }
