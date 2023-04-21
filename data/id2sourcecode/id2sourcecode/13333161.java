    private static void addDir(ZipOutputStream zout, String name) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        zout.putNextEntry(entry);
        zout.closeEntry();
    }
