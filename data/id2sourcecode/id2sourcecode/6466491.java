    public ZipEntryOutputStream(ZipOutputStream out, ZipEntry entry) throws IOException {
        this(out);
        out.putNextEntry(entry);
    }
