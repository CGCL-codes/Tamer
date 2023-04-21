    public long getLastModified(Object src) {
        final ResourceInfo si = (ResourceInfo) src;
        if (si.url != null) {
            try {
                return si.url.openConnection().getLastModified();
            } catch (IOException ex) {
                return -1;
            }
        }
        return si.file.lastModified();
    }
