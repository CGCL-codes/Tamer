    private long getVersion(URL url) throws CacheException {
        URLConnection con;
        try {
            con = url.openConnection();
        } catch (IOException e) {
            throw new CacheException(e);
        }
        long modified = con.getLastModified();
        return modified;
    }
