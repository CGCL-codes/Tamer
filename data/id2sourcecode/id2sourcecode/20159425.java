    public InputStream getInputStream() {
        try {
            URLConnection urlConn = resource.openConnection();
            lastModified = urlConn.getLastModified();
            return urlConn.getInputStream();
        } catch (IOException e) {
            throw new ExecutionException("The file '" + resource.getPath() + "' could not be located", null, e);
        }
    }
