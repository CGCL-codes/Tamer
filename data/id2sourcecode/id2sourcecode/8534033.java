    public InputStream getInputStream() throws IOException {
        if (url != null) return url.openStream();
        return null;
    }
