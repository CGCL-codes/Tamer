    public static ByteBuffer readURLContentToBuffer(URL url, boolean allocateDirect) throws IOException {
        if (url == null) {
            String message = Logging.getMessage("nullValue.URLIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        InputStream is = null;
        try {
            is = url.openStream();
            return readStreamToBuffer(is, allocateDirect);
        } finally {
            WWIO.closeStream(is, url.toString());
        }
    }
