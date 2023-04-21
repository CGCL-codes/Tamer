    public static InputStream getInputStreamForURL(URL url) throws Exception {
        try {
            return url.openStream();
        } catch (ZipException e) {
            return ZipJarUtil.getInputStreamForURL(url);
        }
    }
