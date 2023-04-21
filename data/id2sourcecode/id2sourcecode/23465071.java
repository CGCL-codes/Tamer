    public static InputStream getAsStream(String name) {
        try {
            URL url = getAsURL(name);
            return url.openStream();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
