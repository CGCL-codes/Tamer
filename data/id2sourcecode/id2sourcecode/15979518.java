    private static void compressDir(File dir, ZipOutputStream zos, String basePath) throws IOException {
        File[] files = dir.listFiles();
        if (files.length < 1) {
            ZipEntry entry = new ZipEntry(basePath + dir.getName() + PATH);
            zos.putNextEntry(entry);
            zos.closeEntry();
        }
        for (File file : files) {
            compress(file, zos, basePath + dir.getName() + PATH);
        }
    }
