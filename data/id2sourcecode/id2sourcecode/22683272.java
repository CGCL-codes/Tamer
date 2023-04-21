    public InputStream getInputStream(String fileName) {
        try {
            URL url = new URL(_app.getCodeBase(), fileName);
            return url.openStream();
        } catch (Exception ex) {
            return null;
        }
    }
