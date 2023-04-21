    public static void copyStreamToZip(String entryName, InputStream is, ZipOutputStream zos) throws Exception {
        ZipEntry entry = new ZipEntry(entryName);
        zos.putNextEntry(entry);
        copyStreamToStream(is, zos);
        zos.closeEntry();
    }
