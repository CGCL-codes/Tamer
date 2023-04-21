    public static InputStream openStream(Object src) throws Exception {
        if (src == null || WWUtil.isEmpty(src)) {
            String message = Logging.getMessage("nullValue.SourceIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        if (src instanceof InputStream) {
            return (InputStream) src;
        } else if (src instanceof URL) {
            return ((URL) src).openStream();
        } else if (src instanceof URI) {
            return ((URI) src).toURL().openStream();
        } else if (src instanceof File) {
            Object streamOrException = getFileOrResourceAsStream(((File) src).getPath(), null);
            if (streamOrException instanceof Exception) {
                throw (Exception) streamOrException;
            }
            return (InputStream) streamOrException;
        } else if (!(src instanceof String)) {
            String message = Logging.getMessage("generic.UnrecognizedSourceType", src.toString());
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        String sourceName = (String) src;
        URL url = WWIO.makeURL(sourceName);
        if (url != null) return url.openStream();
        Object streamOrException = getFileOrResourceAsStream(sourceName, null);
        if (streamOrException instanceof Exception) {
            throw (Exception) streamOrException;
        }
        return (InputStream) streamOrException;
    }
