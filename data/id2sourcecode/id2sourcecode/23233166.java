    private ZipEntry zip(String entryName, long time, InputStream in, ZipOutputStream out) throws IOException {
        ZipEntry entry = new ZipEntry(entryName);
        entry.setTime(time);
        out.putNextEntry(entry);
        copy(in, out);
        out.closeEntry();
        return entry;
    }
