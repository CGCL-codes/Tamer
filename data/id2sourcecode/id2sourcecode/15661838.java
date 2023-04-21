    public InputStream getInputStream() throws IOException {
        if (file != null) {
            return new BufferedInputStream(new FileInputStream(file));
        } else if (url != null) {
            return new BufferedInputStream(url.openStream());
        }
        return null;
    }
