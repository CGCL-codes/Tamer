    public InputStream getInputStream() throws VFSException {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new VFSIOException(e);
        }
    }
