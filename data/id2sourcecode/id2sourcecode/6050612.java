    public InputStream getInputStream() throws IOException {
        try {
            return url.openStream();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
