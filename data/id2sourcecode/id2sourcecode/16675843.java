    public static InputStream findInputStream(String name) throws FileNotFoundException {
        try {
            URL url = Utils.class.getResource(name);
            return url.openStream();
        } catch (Exception e) {
            return new FileInputStream(name);
        }
    }
