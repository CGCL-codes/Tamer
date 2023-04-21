    public static InputStream findAsStream(String resource) {
        try {
            URL url = find(resource);
            if (url != null) {
                return url.openStream();
            }
        } catch (IOException ex) {
            Logger.getLogger(ResourceLocator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
