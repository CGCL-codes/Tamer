    private void backupFiles(File destFile, boolean ignoreLastBackup) throws IOException {
        List dataFiles = getFilenamesToBackup();
        if (dataFiles == null || dataFiles.size() == 0) return;
        Collections.sort(dataFiles);
        ProfTimer pt = new ProfTimer(IncrementalDirectoryBackup.class, "IncrementalDirectoryBackup.backupFiles");
        File dataDir = srcDirectory;
        File backupDir = destDirectory;
        File[] backupFiles = getBackupFiles(backupDir);
        File mostRecentBackupFile = (ignoreLastBackup ? null : findMostRecentBackupFile(backupFiles));
        File oldBackupTempFile = new File(backupDir, OLD_BACKUP_TEMP_FILENAME);
        File newBackupTempFile = new File(backupDir, NEW_BACKUP_TEMP_FILENAME);
        ZipOutputStream newBackupOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(newBackupTempFile)));
        newBackupOut.setLevel(9);
        boolean wroteHistLog = false;
        if (mostRecentBackupFile != null) {
            ZipInputStream oldBackupIn = new ZipInputStream(new TimedInputStream(new BufferedInputStream(new FileInputStream(mostRecentBackupFile)), 60000));
            ZipOutputStream oldBackupOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(oldBackupTempFile)));
            oldBackupOut.setLevel(9);
            oldBackupIsEmpty = true;
            atomicFilesInOldBackup = new ArrayList();
            ZipEntry oldEntry;
            while ((oldEntry = oldBackupIn.getNextEntry()) != null) {
                String filename = oldEntry.getName();
                ThreadThrottler.tick();
                if (HIST_LOG_FILE_NAME.equals(filename)) {
                    long histLogModTime = oldEntry.getTime();
                    if (histLogModTime < 1) histLogModTime = mostRecentBackupFile.lastModified();
                    File logFile = new File(dataDir, LOG_FILE_NAME);
                    long currentLogModTime = logFile.lastModified();
                    if (currentLogModTime <= histLogModTime) copyZipEntry(oldBackupIn, newBackupOut, oldEntry, null); else writeHistLogFile(oldBackupIn, newBackupOut, dataDir);
                    wroteHistLog = true;
                    continue;
                }
                if (!fileFilter.accept(srcDirectory, filename)) continue;
                File file = new File(dataDir, filename);
                if (dataFiles.remove(filename)) {
                    backupFile(oldEntry, oldBackupIn, oldBackupOut, newBackupOut, file, filename);
                } else {
                    copyZipEntry(oldBackupIn, oldBackupOut, oldEntry, null);
                    wroteEntryToOldBackup(filename);
                }
            }
            addAtomicFilesToBackup(oldBackupOut);
            oldBackupIn.close();
            mostRecentBackupFile.delete();
            if (oldBackupIsEmpty) {
                oldBackupOut.putNextEntry(new ZipEntry("foo"));
                oldBackupOut.close();
                oldBackupTempFile.delete();
            } else {
                oldBackupOut.close();
                FileUtils.renameFile(oldBackupTempFile, mostRecentBackupFile);
            }
        }
        for (Iterator iter = dataFiles.iterator(); iter.hasNext(); ) {
            ThreadThrottler.tick();
            String filename = (String) iter.next();
            File file = new File(dataDir, filename);
            backupFile(null, null, null, newBackupOut, file, filename);
        }
        if (wroteHistLog == false) writeHistLogFile(null, newBackupOut, dataDir);
        pt.click("Backed up data files");
        if (extraContentSupplier != null) extraContentSupplier.addExtraContentToBackup(newBackupOut);
        pt.click("Backed up extra content");
        newBackupOut.close();
        FileUtils.renameFile(newBackupTempFile, destFile);
    }
