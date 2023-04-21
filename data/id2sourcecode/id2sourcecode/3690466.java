    public InputStream openTextureStream(String name) {
        try {
            URL url = new URL(baseUrl, name);
            return (url.openStream());
        } catch (IOException ex) {
            return (null);
        }
    }
