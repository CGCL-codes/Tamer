    public InputStream getInputStream() throws FileSystemException {
        InputStream stream;
        try {
            stream = url.openStream();
        } catch (java.io.IOException ioe) {
            throw new FileSystemException(X_IO_ERROR, ioe);
        }
        return stream;
    }
