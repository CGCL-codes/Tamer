    public static boolean validateUrl(String url) {
        try {
            URL _url = new URL(url);
            _url.openConnection().connect();
            return true;
        } catch (Exception e) {
            log.log(Level.FINE, "Failed validating url " + url, e);
            return false;
        }
    }
