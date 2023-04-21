    public static void zipDir(File dir2zip, File zipFilePath) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath));
        zos.putNextEntry(new ZipEntry(dir2zip.getName() + '/'));
        zipDir(dir2zip, zos);
        zos.close();
    }
