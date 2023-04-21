    private static void createDir(ZipOutputStream zos, String name) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        zos.putNextEntry(entry);
        zos.closeEntry();
    }
