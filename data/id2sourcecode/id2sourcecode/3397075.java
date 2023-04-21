    public static InputStream getStream(String ressource) {
        try {
            if (!ressource.matches("http://.*|file:.*")) ressource = "file:" + ressource;
            URL url = new URL(ressource);
            return url.openStream();
        } catch (Exception e) {
            throw (new RuntimeException(e));
        }
    }
