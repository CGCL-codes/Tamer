    public static XmlConfigurator getInstance(URL url, Boolean validate) throws java.io.IOException {
        InputStream is = url.openStream();
        try {
            return getInstance(is, validate);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                log.warn("Failed to close InputStream", e);
            }
        }
    }
