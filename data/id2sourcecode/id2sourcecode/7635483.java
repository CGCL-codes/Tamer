    public long getLastModified(Object src) {
        if (getCheckPeriod() < 0) return 1;
        try {
            final URL url = getExtendletContext().getResource((String) src);
            return url != null ? url.openConnection().getLastModified() : -1;
        } catch (Throwable ex) {
            return -1;
        }
    }
