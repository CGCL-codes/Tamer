    public void write(ZipOutputStream out) throws IOException {
        if (isEmpty()) return;
        ZipEntry entry = new ZipEntry(ARCHIVE_PATH + "/" + MANIFEST_FILE_NAME);
        out.putNextEntry(entry);
        write((OutputStream) out);
        out.closeEntry();
    }
