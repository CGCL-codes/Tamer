    public ArchiveEntry getNextEntry() throws IOException {
        boolean found = false;
        TarEntry tarEntry;
        do {
            tarEntry = tarStream.getNextEntry();
            if (tarEntry == null) {
                return null;
            }
            if (!tarEntry.isDirectory()) {
                found = true;
            }
        } while (found == false);
        String entryName = tarEntry.getName();
        if (entryName.lastIndexOf("/") != -1) {
            entryName = entryName.substring(entryName.lastIndexOf("/") + 1);
        } else if (entryName.indexOf("\\") != -1) {
            entryName = entryName.substring(entryName.lastIndexOf("\\") + 1);
        }
        File entryTempFile = File.createTempFile("archive_entry", entryName);
        entryTempFile.deleteOnExit();
        FileOutputStream tempFileOS = new FileOutputStream(entryTempFile);
        byte[] readBuff = new byte[10 * 1024];
        int bytesRead = tarStream.read(readBuff);
        while (bytesRead > 0) {
            tempFileOS.write(readBuff, 0, bytesRead);
            bytesRead = tarStream.read(readBuff);
        }
        ArchiveEntry archiveEntry = new ArchiveEntry(tarEntry.getName(), entryTempFile.getAbsolutePath());
        archiveEntry.setOriginalFileDate(tarEntry.getModTime());
        archiveEntry.setOriginalSize(tarEntry.getSize());
        return archiveEntry;
    }
