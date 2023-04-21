    private static void zipDir(File dir, ZipOutputStream zos, String prefix) {
        File[] entries = dir.listFiles();
        for (int i = 0; i < entries.length; i++) {
            if (entries[i].isDirectory()) {
                ZipEntry zi = new ZipEntry(prefix + "/" + entries[i].getName() + "/");
                try {
                    zos.putNextEntry(zi);
                    zos.closeEntry();
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                }
                zipDir(entries[i], zos, prefix + "/" + entries[i].getName());
            } else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(entries[i]);
                    ZipEntry zi = new ZipEntry(prefix + "/" + entries[i].getName());
                    zos.putNextEntry(zi);
                    copystream(fis, zos);
                    zos.closeEntry();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                } finally {
                    try {
                        if (fis != null) fis.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
