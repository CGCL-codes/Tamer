    public Reader getReader() {
        try {
            return new InputStreamReader(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException("Resource missing: " + url);
        }
    }
