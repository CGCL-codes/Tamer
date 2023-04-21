    public void save() throws IOException {
        File parentDirectory = archiveFile.getCanonicalFile().getParentFile();
        File tempFile = File.createTempFile("archive", null, parentDirectory);
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempFile));
        try {
            List nameList = new ArrayList(archiveEntries.keySet());
            Collections.sort(nameList);
            Iterator names = nameList.iterator();
            while (names.hasNext()) {
                String name = (String) names.next();
                ZipArchiveEntry entry = (ZipArchiveEntry) archiveEntries.get(name);
                if (entry != null) {
                    ZipEntry zipEntry = new ZipEntry(entry.getName());
                    zipEntry.setTime(entry.getTime());
                    zos.putNextEntry(zipEntry);
                    zos.write(entry.getContent());
                    zos.closeEntry();
                }
            }
        } finally {
            zos.flush();
            zos.close();
        }
        if (archiveFile.exists()) {
            File backupArchiveFile = new File(archiveFile + ".bak");
            if (backupArchiveFile.exists()) {
                if (!backupArchiveFile.delete()) {
                    throw new IOException("Unable to delete old backup file " + backupArchiveFile);
                }
            }
            System.gc();
            if (!archiveFile.renameTo(backupArchiveFile)) {
                throw new IOException("Unable to rename archive file " + archiveFile + " to backup file " + backupArchiveFile);
            }
            if (!tempFile.renameTo(archiveFile)) {
                if (backupArchiveFile.renameTo(archiveFile)) {
                    throw new IOException("Unable to rename temporary file " + tempFile + " to new archive file " + archiveFile + ", restored old archive file.");
                } else {
                    throw new IOException("Unable to rename temporary file " + tempFile + " to archive file " + archiveFile + ", and unable to restore old archive file from " + "backup file " + backupArchiveFile + ". " + "Please record this message and contact technical " + "support.");
                }
            } else {
                backupArchiveFile.delete();
            }
        } else {
            if (!tempFile.renameTo(archiveFile)) {
                throw new IOException("Unable to rename temporary file " + tempFile + " to new archive file " + archiveFile);
            }
        }
    }
