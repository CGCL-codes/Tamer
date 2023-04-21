    public void saveBackup(final boolean bFullBackup) throws BackupIOError, NoFileError, BackupError {
        try {
            String sBackupName = new File(this.docController.getPathUtils().getSystemPathFromFileURL(this.docController.getFileURL())).getName();
            if (sBackupName != null && !sBackupName.isEmpty()) {
                sBackupName = sBackupName.substring(0, sBackupName.lastIndexOf("."));
                Backup newBackupDir = new Backup(this.backupDir, sBackupName, new Date(), bFullBackup);
                String sFullFileName = this.backupDir + newBackupDir.getBackupName() + "-" + this.dateFormat.format(newBackupDir.getCreated()) + ".odt";
                if (!newBackupDir.exists()) {
                    newBackupDir.mkdirs();
                }
                if (newBackupDir.isDirectory()) {
                    this.storeToFile(sFullFileName);
                }
                if (bFullBackup) {
                    File documentSelf = new File(sFullFileName);
                    this.docController.exit(true);
                    File backupSrcDBDir = new File(newBackupDir.getAbsolutePath() + File.separator + "sourceDB");
                    File srcSrcDB = new File(this.docController.getPathUtils().getSystemPathFromFileURL(this.docController.getPathUtils().getPath(PathUtils.USER)) + File.separator + this.docController.getConfigHandler().getOOoProperty("txtSource.SourceDB"));
                    File backupSrcDB = new File(newBackupDir.getAbsolutePath() + File.separator + "sourceDB" + File.separator + srcSrcDB.getName());
                    if (backupSrcDBDir.mkdirs()) {
                        FileUtils.copy(srcSrcDB, backupSrcDB);
                    }
                    File backupQuotDBDir = new File(newBackupDir.getAbsolutePath() + File.separator + "quotDB");
                    File srcQuotDB = new File(this.docController.getPathUtils().getSystemPathFromFileURL(this.docController.getPathUtils().getPath(PathUtils.USER)) + File.separator + this.docController.getConfigHandler().getOOoProperty("txtSource.QuotationStyleDB"));
                    File backupQuotDB = new File(newBackupDir.getAbsolutePath() + File.separator + "quotDB" + File.separator + srcQuotDB.getName());
                    if (backupQuotDBDir.mkdirs()) {
                        FileUtils.copy(srcQuotDB, backupQuotDB);
                    }
                    List<File> filenames = new ArrayList<File>();
                    filenames.add(documentSelf);
                    filenames.add(backupSrcDBDir);
                    filenames.add(backupQuotDBDir);
                    String sSourceBackupDir = this.docController.getConfigHandler().getOOoProperty("txtSource.BackupDir");
                    if (!sSourceBackupDir.isEmpty()) {
                        File backupSrcDir = new File(newBackupDir.getAbsolutePath() + File.separator + "sources");
                        File srcSrc = new File(sSourceBackupDir);
                        if (backupSrcDir.mkdirs() && srcSrc.isDirectory() && FileUtils.getFiles(srcSrc, null) != null) {
                            for (final File curFile : FileUtils.getFiles(srcSrc, null)) {
                                FileUtils.copy(curFile.getAbsolutePath(), backupSrcDir + File.separator + curFile.getName());
                            }
                            filenames.add(backupSrcDir);
                        } else {
                            this.docController.getLogger().warning("Fullbackup: Skipped invalid source directory");
                        }
                    }
                    String sZipFileName = this.backupDir + sBackupName + "-" + this.dateFormat.format(newBackupDir.getCreated()) + ".zip";
                    FileUtils.zip((File[]) filenames.toArray(new File[] {}), sZipFileName, newBackupDir.toURI(), null);
                    this.docController.getSourceController();
                    for (File delFile : filenames) FileUtils.deleteDir(delFile, null);
                }
                this.docController.getLogger().fine("Backup was stored to " + sFullFileName);
            } else {
                throw new NoFileError("Backup could not be saved. A new document must be saved first time before backup.");
            }
        } catch (final ConfigHandlerError e) {
            throw new BackupError(e.getLogMessage(), e);
        } catch (final DBError e) {
            throw new BackupError(e.getLogMessage(), e);
        } catch (final FileError e) {
            throw new BackupIOError("Write error during backup process.", e);
        }
    }
