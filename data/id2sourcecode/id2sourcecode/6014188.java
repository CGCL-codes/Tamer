    public InputStream getResourceAsStream(String path) {
        try {
            return new URL(worldurl, path).openStream();
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
