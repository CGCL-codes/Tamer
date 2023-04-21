        @Override
        public void run() {
            FileManager fileMgr = Phex.getFileManager();
            File libraryFile = Environment.getInstance().getPhexConfigFile(EnvironmentConstants.XML_SHARED_LIBRARY_FILE_NAME);
            File tmpFile = Environment.getInstance().getPhexConfigFile(EnvironmentConstants.XML_SHARED_LIBRARY_FILE_NAME + ".tmp");
            do {
                logger.debug("Saving shared library.");
                isFollowUpSaveTriggered = false;
                rwLock.readLock();
                try {
                    DPhex dPhex = new DPhex();
                    dPhex.setPhexVersion(PhexVersion.getFullVersion());
                    DSharedLibrary dLibrary = createDSharedLibrary();
                    dPhex.setSharedLibrary(dLibrary);
                    ManagedFile tmpMgFile = fileMgr.getReadWriteManagedFile(tmpFile);
                    XMLBuilder.saveToFile(tmpMgFile, dPhex);
                    ManagedFile libraryMgFile = fileMgr.getReadWriteManagedFile(libraryFile);
                    try {
                        libraryMgFile.acquireFileLock();
                        FileUtils.copyFile(tmpFile, libraryFile);
                    } finally {
                        libraryMgFile.releaseFileLock();
                    }
                } catch (ManagedFileException exp) {
                    if (exp.getCause() instanceof InterruptedException) {
                        logger.debug(exp.toString());
                    } else {
                        logger.error(exp.toString(), exp);
                        Environment.getInstance().fireDisplayUserMessage(UserMessageListener.SharedFilesSaveFailed, new String[] { exp.toString() });
                    }
                } catch (IOException exp) {
                    logger.error(exp.toString(), exp);
                    Environment.getInstance().fireDisplayUserMessage(UserMessageListener.SharedFilesSaveFailed, new String[] { exp.toString() });
                } finally {
                    try {
                        rwLock.readUnlock();
                    } catch (IllegalAccessException exp) {
                        logger.error(exp.toString(), exp);
                    }
                }
            } while (isFollowUpSaveTriggered);
            logger.debug("Finished saving download list...");
            synchronized (saveSharedFilesLock) {
                saveSharedFilesJob = null;
            }
        }
