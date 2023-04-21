    public static InputSource gzipUncompressToInputSource(URL url) {
        try {
            return new InputSource(new BufferedInputStream((new GZIPInputStream(url.openStream()))));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
