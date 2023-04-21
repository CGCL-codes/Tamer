    public static InputStream findResourceInClasspath(String name) {
        try {
            URL url = findURLInClasspath(name);
            if (url != null) {
                logObj.debug("resource found in classpath: " + url);
                return url.openStream();
            } else {
                logObj.debug("resource not found in classpath: " + name);
                return null;
            }
        } catch (IOException ioex) {
            return null;
        }
    }
