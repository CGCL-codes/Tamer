    public DSharedLibrary loadSharedLibrary() {
        logger.debug("Load shared library configuration file.");
        File file = Environment.getInstance().getPhexConfigFile(EnvironmentConstants.XML_SHARED_LIBRARY_FILE_NAME);
        DPhex dPhex;
        try {
            ManagedFile managedFile = Phex.getFileManager().getReadWriteManagedFile(file);
            dPhex = XMLBuilder.loadDPhexFromFile(managedFile);
            if (dPhex == null) {
                logger.debug("No shared library configuration file found.");
                return null;
            }
        } catch (InterruptedIOException exp) {
            return null;
        } catch (IOException exp) {
            logger.error(exp.toString(), exp);
            Environment.getInstance().fireDisplayUserMessage(UserMessageListener.SharedFilesLoadFailed, new String[] { exp.toString() });
            try {
                FileUtils.copyFile(file, new File(file.getAbsolutePath() + ".failed"));
            } catch (IOException e) {
                logger.error("Failed to store failed file copy: {}", e);
            }
            return null;
        } catch (ManagedFileException exp) {
            logger.error(exp.toString(), exp);
            Environment.getInstance().fireDisplayUserMessage(UserMessageListener.SharedFilesLoadFailed, new String[] { exp.toString() });
            return null;
        }
        DSharedLibrary sharedLibrary = dPhex.getSharedLibrary();
        return sharedLibrary;
    }
