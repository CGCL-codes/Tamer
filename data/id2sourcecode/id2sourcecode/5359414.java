    public static void copyFileToZip(String entryName, String filename, ZipOutputStream zos) throws Exception {
        ZipEntry entry = new ZipEntry(entryName);
        zos.putNextEntry(entry);
        FileInputStream fis = new FileInputStream(filename);
        copyStreamToStream(new FileInputStream(filename), zos);
        fis.close();
        zos.closeEntry();
    }
