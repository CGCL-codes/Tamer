    public boolean isOutOfDate(URL url) throws IOException {
        if (!this.isCached(url)) {
            return true;
        }
        final URLConnection connection = url.openConnection();
        final long lastModifiedRaw = connection.getLastModified();
        final Date cacheDate = this.getCacheDate(connection.getURL());
        if ((lastModifiedRaw > 0) && (cacheDate != null)) {
            final Date lastModified = new Date(lastModifiedRaw);
            return lastModified.after(cacheDate);
        }
        final String eTag = connection.getHeaderField("ETag");
        final String cacheETag = this.getCacheETag(url);
        if ((eTag != null) && (cacheETag != null)) {
            return !eTag.equals(cacheETag);
        }
        return true;
    }
