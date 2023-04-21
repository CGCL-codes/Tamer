    public static InputStream openURLInputStream(String urlName) throws IOException {
        URL url;
        try {
            url = new URL(urlName);
            return url.openStream();
        } catch (IOException ioe) {
            throw ioe;
        } catch (Throwable e2) {
            throw new IOException(e2.getMessage());
        }
    }
