    public static void unzipToDir(InputStream zipInputStream, File destDir) throws IOException {
        ZipInputStream zis = new ZipInputStream(zipInputStream);
        ZipEntry zipEntry;
        destDir.mkdirs();
        while ((zipEntry = zis.getNextEntry()) != null) {
            File file = new File(destDir, PathHelper.getRealPathFromSafeForm(zipEntry.getName()));
            if (zipEntry.isDirectory()) {
                file.mkdirs();
            } else {
                new File(file.getParent()).mkdirs();
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(readInputStreamToByteArray(zis));
                fos.close();
            }
        }
        zis.close();
    }
