    private boolean isConnected() {
        try {
            URL url = new URL("http://www.google.com");
            url.openConnection();
            url.openStream();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
