    protected URLConnection getURLConnection(String urlString) {
        try {
            URL url = new URL(urlString);
            return url.openConnection();
        } catch (Exception e) {
            return null;
        }
    }
