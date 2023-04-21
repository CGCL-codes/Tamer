    @Override
    public void putNextEntry(ArchiveEntry entry) throws IOException {
        zos.putNextEntry(new ZipEntry(entry.getFileName()));
    }
