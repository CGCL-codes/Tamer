    private void saveConfiguration(ZipOutputStream zipStream) throws IOException {
        logger.info("Saving configuration file");
        currentSavedObjectName = "configuration";
        zipStream.putNextEntry(new ZipEntry(CONFIG_FILENAME));
        File tempConfigFile = File.createTempFile("mzmineconfig", ".tmp");
        try {
            MZmineCore.getConfiguration().saveConfiguration(tempConfigFile);
        } catch (Exception e) {
            throw new IOException("Could not save configuration" + ExceptionUtils.exceptionToString(e));
        }
        FileInputStream fileStream = new FileInputStream(tempConfigFile);
        StreamCopy copyMachine = new StreamCopy();
        copyMachine.copy(fileStream, zipStream);
        fileStream.close();
        tempConfigFile.delete();
    }
