    private URLConnection openConnection(String link) {
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            log.debug("Incorrect URL " + link, e);
            return null;
        }
        try {
            URLConnection connection = url.openConnection();
            connection.setReadTimeout(10 * 1000);
            return connection;
        } catch (IOException e1) {
            log.debug("Could not fetch " + link, e1);
            return null;
        }
    }
