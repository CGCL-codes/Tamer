    public static <R extends Properties> R loadProperties(URL url, R retVal) throws IOException {
        AssertUtils.assertNonNullArg(url);
        AssertUtils.assertNonNullArg(retVal);
        try {
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            try {
                retVal.load(in);
            } finally {
                in.close();
            }
        } catch (Exception ex) {
            throw new IOException("Failed to connect to URL. url=" + url);
        }
        return retVal;
    }
