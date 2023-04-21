    private void upgradeArchive(File newArchiveFile, File oldArchive, String relativePath) throws IOException {
        if (changed(newArchiveFile, oldArchive)) {
            log.log("Upgrading archive " + newArchiveFile.getAbsolutePath());
            ZipEntry diffEntry = new ZipEntry(relativePath);
            diffEntry.setSize(newArchiveFile.length());
            outputStream.putNextEntry(diffEntry);
            ZipOutputStream diffOutput = new ZipOutputStream(outputStream);
            diffOutput.setLevel(ZipOutputStream.STORED);
            ZipFile newFile = new ZipFile(newArchiveFile);
            ZipFile oldFile = new ZipFile(oldArchive);
            Enumeration<? extends ZipEntry> entries = newFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry newEntry = entries.nextElement();
                oldFiles.remove(archiveFile(oldArchive, newEntry.getName()));
                ZipEntry oldEntry = findInArchive(oldFile, newEntry.getName());
                byte[] newData = readEntry(newFile, newEntry);
                boolean writeToOutput = false;
                if (oldEntry == null) {
                    writeToOutput = true;
                } else {
                    byte[] oldData = readEntry(oldFile, oldEntry);
                    if (newData.length != oldData.length) {
                        writeToOutput = true;
                    } else {
                        for (int i = 0; i < newData.length; i++) {
                            if (newData[i] != oldData[i]) {
                                writeToOutput = true;
                            }
                        }
                    }
                }
                if (writeToOutput) {
                    writeEntryToStream(diffOutput, newEntry, newData);
                }
            }
            newFile.close();
            oldFile.close();
        } else {
            log.log("Unchanged archive " + newArchiveFile.getAbsolutePath());
            unchanged.add(oldArchive.getAbsolutePath());
        }
    }
