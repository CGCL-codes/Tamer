    private WebsiteEntry getNextEntry(ZipInputStream zipStream, File tempStagingDir) throws IOException {
        boolean found = false;
        ZipEntry zipEntry;
        do {
            zipEntry = zipStream.getNextEntry();
            if (zipEntry == null) {
                return null;
            }
            if (!zipEntry.isDirectory()) {
                found = true;
            }
        } while (found == false);
        File entryTempFile = new File(tempStagingDir, zipEntry.getName());
        entryTempFile.getParentFile().mkdirs();
        entryTempFile.deleteOnExit();
        FileOutputStream tempFileOS = new FileOutputStream(entryTempFile);
        byte[] readBuff = new byte[10 * 1024];
        int bytesRead = zipStream.read(readBuff);
        while (bytesRead > 0) {
            tempFileOS.write(readBuff, 0, bytesRead);
            bytesRead = zipStream.read(readBuff);
        }
        WebsiteEntry archiveEntry = new WebsiteEntry(zipEntry.getName(), entryTempFile.getAbsolutePath());
        archiveEntry.setOriginalFileDate(new Date(zipEntry.getTime()));
        archiveEntry.setOriginalSize(zipEntry.getSize());
        return archiveEntry;
    }
