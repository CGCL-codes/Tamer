    private static void addFile(ZipOutputStream zos, String name, InputStream is) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        zos.putNextEntry(entry);
        BinaryFile.copy(is, zos, true, false);
        zos.closeEntry();
    }
