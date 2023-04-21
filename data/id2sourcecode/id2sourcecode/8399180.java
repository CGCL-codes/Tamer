    public static ChannelFormat getFormat(URL url) throws IOException, UnsupportedFormatException {
        logger.info("Trying to retrieve stream from " + url);
        BufferedInputStream in = new BufferedInputStream(url.openStream(), NR_FIRST_BYTES);
        return getFormat(in);
    }
