    public ArchiveEntry getNextEntry() throws IOException {
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
        String entryName = zipEntry.getName();
        if (entryName.lastIndexOf("/") != -1) {
            entryName = entryName.substring(entryName.lastIndexOf("/") + 1);
        } else if (entryName.indexOf("\\") != -1) {
            entryName = entryName.substring(entryName.lastIndexOf("\\") + 1);
        }
        File entryTempFile = File.createTempFile("archive_entry", entryName);
        entryTempFile.deleteOnExit();
        FileOutputStream tempFileOS = new FileOutputStream(entryTempFile);
        byte[] readBuff = new byte[10 * 1024];
        int bytesRead = zipStream.read(readBuff);
        while (bytesRead > 0) {
            tempFileOS.write(readBuff, 0, bytesRead);
            bytesRead = zipStream.read(readBuff);
        }
        ArchiveEntry archiveEntry = new ArchiveEntry(zipEntry.getName(), entryTempFile.getAbsolutePath());
        archiveEntry.setOriginalFileDate(new Date(zipEntry.getTime()));
        archiveEntry.setOriginalSize(zipEntry.getSize());
        return archiveEntry;
    }
