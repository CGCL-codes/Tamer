    public void putFileAsZipEntries(ZipOutputStream zos, File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                this.putFileAsZipEntries(zos, files[i]);
            }
        } else {
            try {
                zos.putNextEntry(new ZipEntry(file.getAbsolutePath().substring(file.getAbsolutePath().indexOf(File.separator) + 1)));
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                copyStreamsWithoutClose(bis, zos, new byte[1024 * 20]);
                bis.close();
                zos.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
