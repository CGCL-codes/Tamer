    public long getSize() {
        try {
            return url.openStream().available();
        } catch (IOException e) {
            throw new RuntimeException("Resource missing: " + url);
        }
    }
