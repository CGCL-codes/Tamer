    private static void doZip(ZipOutputStream zos, java.io.File file, String base) throws IOException {
        byte[] buff = new byte[2048];
        base += file.getName();
        if (file.isDirectory()) {
            base += '/';
            zos.putNextEntry(new ZipEntry(base));
            zos.closeEntry();
            java.io.File[] list = file.listFiles();
            for (java.io.File temp : list) {
                doZip(zos, temp, base);
            }
        } else {
            zos.putNextEntry(new ZipEntry(base));
            saveEntry(zos, file, buff);
            zos.closeEntry();
        }
    }
