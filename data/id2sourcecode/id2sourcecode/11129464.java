    public void addFolderToArchive(ZipOutputStream l_zipStream, File l_folder, File l_baseFolder) throws IOException {
        File[] files = l_folder.listFiles();
        for (File file : files) {
            String zipPath = file.getCanonicalPath().substring(l_baseFolder.getParentFile().getCanonicalPath().length() + 1);
            if (file.isDirectory()) {
                ZipEntry zipEntry = new ZipEntry(zipPath + "/");
                zipEntry.setTime(this.convertToZipTime(file.lastModified()));
                l_zipStream.putNextEntry(zipEntry);
                this.addFolderToArchive(l_zipStream, file, l_baseFolder);
            } else {
                ZipEntry zipEntry = new ZipEntry(zipPath);
                zipEntry.setTime(this.convertToZipTime(file.lastModified()));
                l_zipStream.putNextEntry(zipEntry);
                InputStream inStream = new FileInputStream(file);
                this.copy(inStream, l_zipStream);
                inStream.close();
                l_zipStream.closeEntry();
            }
        }
    }
