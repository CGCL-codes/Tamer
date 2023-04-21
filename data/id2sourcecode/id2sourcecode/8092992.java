    public void setHeader(PkgMapEntryHeader header) {
        if (this.header != null) {
            throw new IllegalStateException("The header is already set. Cannot overwrite");
        }
        if (header == null) {
            log.info("Resetting pkgmap header to null");
        }
        this.header = header;
    }
