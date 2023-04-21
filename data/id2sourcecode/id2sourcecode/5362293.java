    @Override
    public ArchiveEntry getNextEntry() throws IOException {
        ZipEntry local = zis.getNextEntry();
        if (null != local) return new ZipFileEntry(local);
        log.warning("Returning a NULL entry");
        return null;
    }
