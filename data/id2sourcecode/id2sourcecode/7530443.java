        @Override
        public void run() {
            do {
                NLogger.debug(SwarmingManager.class, "Start saving download list...");
                downloadListChangedSinceSave = false;
                isFollowUpSaveTriggered = false;
                try {
                    DPhex dPhex = new DPhex();
                    dPhex.setPhexVersion(PhexVersion.getFullVersion());
                    DSubElementList<DDownloadFile> dList = createDDownloadList();
                    dPhex.setDownloadList(dList);
                    File downloadFile = Environment.getInstance().getPhexConfigFile(EnvironmentConstants.XML_DOWNLOAD_FILE_NAME);
                    File downloadFileBak = new File(downloadFile.getAbsolutePath() + ".bak");
                    ManagedFile managedFile = Phex.getFileManager().getReadWriteManagedFile(downloadFileBak);
                    XMLBuilder.saveToFile(managedFile, dPhex);
                    FileUtils.copyFile(downloadFileBak, downloadFile);
                } catch (ManagedFileException exp) {
                    NLogger.error(SwarmingManager.class, exp, exp);
                    Environment.getInstance().fireDisplayUserMessage(UserMessageListener.DownloadSettingsSaveFailed, new String[] { exp.toString() });
                } catch (IOException exp) {
                    NLogger.error(SwarmingManager.class, exp, exp);
                    Environment.getInstance().fireDisplayUserMessage(UserMessageListener.DownloadSettingsSaveFailed, new String[] { exp.toString() });
                }
            } while (isFollowUpSaveTriggered);
            NLogger.debug(SwarmingManager.class, "Finished saving download list...");
            synchronized (saveDownloadListLock) {
                saveDownloadListJob = null;
            }
        }
