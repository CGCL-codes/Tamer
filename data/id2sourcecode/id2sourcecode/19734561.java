    private static String stringWithContentsOfURL(URL url, String encoding) {
        try {
            return stringWithContentsOfReader(new InputStreamReader(url.openStream(), encoding));
        } catch (IOException ex) {
            return null;
        }
    }
