    public static BufferedReader getReader(String urlFragment) throws IOException {
        URL url = getURL(urlFragment);
        if (url == null) {
            return null;
        } else {
            java.io.InputStream inputStream = url.openStream();
            return new BufferedReader(new InputStreamReader(inputStream));
        }
    }
