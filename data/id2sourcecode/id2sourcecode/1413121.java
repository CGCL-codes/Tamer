    private InputSource getCMLType(String type) {
        try {
            URL url = ClassLoader.getSystemResource(ddtResourceDir + type);
            if (url == null) {
                logger.error("Error while trying to read CML DTD (" + type + ") from " + ddtResourceDir);
                return null;
            }
            return new InputSource(new BufferedReader(new InputStreamReader(url.openStream())));
        } catch (Exception e) {
            logger.error("Error while trying to read CML DTD (" + type + ") from " + ddtResourceDir + ":" + e.toString());
            return null;
        }
    }
