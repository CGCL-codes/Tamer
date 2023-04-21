    public static String getContents(URL url) {
        try {
            return getContents(url.openStream());
        } catch (IOException e) {
            return null;
        }
    }
