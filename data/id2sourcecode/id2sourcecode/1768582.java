    private boolean hasDirectoryReadAccess(URI uri) throws InitializationException {
        if (uri == null) {
            throw new InitializationException("rrd resource directory was resolved to 'null'");
        }
        File dir = new File(uri);
        try {
            return dir.exists() && dir.canRead() && dir.canWrite();
        } catch (SecurityException e) {
            throw new InitializationException("Security Manager has denied read access to directory ''{0}''. " + "In order to write rrd data, file write access must be explicitly " + "granted to this directory. ({1})", e, uri, e.getMessage());
        }
    }
