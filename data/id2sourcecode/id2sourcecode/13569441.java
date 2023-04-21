    private URLConnection getURLConnection(URL url) {
        try {
            return url.openConnection();
        } catch (IOException e) {
            return null;
        }
    }
