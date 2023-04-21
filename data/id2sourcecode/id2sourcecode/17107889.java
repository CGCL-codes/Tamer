    private static Properties loadPropertiesFromUrl(final URL url) {
        InputStream stream = null;
        try {
            stream = url.openStream();
            if (stream != null) {
                Properties props = new Properties();
                props.load(stream);
                return props;
            }
        } catch (IOException e) {
            log.error("Unable to read URL " + url, e);
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException e) {
                log.error("error reading URL" + url, e);
            }
        }
        return null;
    }
