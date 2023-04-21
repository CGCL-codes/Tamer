    public long getLastModified(Object src) {
        final ResourceInfo si = (ResourceInfo) src;
        if (si.url != null) {
            final String protocol = si.url.getProtocol().toLowerCase();
            if (!"http".equals(protocol) && !"https".equals(protocol) && !"ftp".equals(protocol)) {
                try {
                    return si.url.openConnection().getLastModified();
                } catch (Throwable ex) {
                    return -1;
                }
            }
            return -1;
        }
        return si.file.lastModified();
    }
