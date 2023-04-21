    private void backupFile(ZipOutputStream zipOut, String filename) throws IOException {
        ZipEntry e = new ZipEntry(filename);
        File file = new File(srcDirectory, filename);
        e.setTime(file.lastModified());
        zipOut.putNextEntry(e);
        FileUtils.copyFile(file, zipOut);
        zipOut.closeEntry();
    }
