    public static InputStream getXMLResourceByUrl(URL url) {
        InputStream in;
        try {
            in = url.openStream();
        } catch (IOException e) {
            Log.logger.error("URL.openStream failed: " + e);
            return null;
        }
        if (GlobalConfig.globalInstance().isResourceCompression()) {
            try {
                return new GZIPInputStream(in);
            } catch (IOException e) {
                Log.logger.error("GZIPInputStream creation failed!", e);
                return null;
            }
        }
        return in;
    }
