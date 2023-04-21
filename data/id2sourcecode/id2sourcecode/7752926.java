    public InputStream getInputStream() throws IOException {
        if (in == null && url != null) {
            if (connection == null) connection = url.openConnection();
            in = connection.getInputStream();
            this.lastModified = connection.getLastModified();
        }
        return in;
    }
