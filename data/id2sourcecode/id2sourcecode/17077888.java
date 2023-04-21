    @Override
    public InputStream getResourceAsStream(String name) {
        URL url = getResource(name);
        if (url == null) return null; else try {
            return url.openStream();
        } catch (IOException e) {
            return null;
        }
    }
