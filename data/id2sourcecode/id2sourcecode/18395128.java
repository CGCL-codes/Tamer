    public OutputStream newEntry(String name) throws IOException {
        ZipEntry entry = new ZipEntry(mkRelative(currentPath) + '/' + name);
        out.putNextEntry(entry);
        dataWritten = true;
        return (out);
    }
