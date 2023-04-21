    public InputStream getInputStream() throws IOException {
        if (file != null) return new FileInputStream(file); else if (url != null) return url.openStream();
        return null;
    }
