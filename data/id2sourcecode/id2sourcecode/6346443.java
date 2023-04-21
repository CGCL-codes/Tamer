    public static InputStream getResourceAsStream(String resourceName, Class callingClass) throws AOSystemException {
        final String METHOD_NAME = "getResourceAsStream(String resourceName, Class callingClass)";
        URL url = getResource(resourceName, callingClass);
        try {
            return (url != null) ? url.openStream() : null;
        } catch (IOException e) {
            LogManager.log(new LogMessage(LogManager.FATAL, CLASS_NAME, METHOD_NAME, e.getMessage()));
            return null;
        }
    }
