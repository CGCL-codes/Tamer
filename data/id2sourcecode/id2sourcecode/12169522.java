    private void saveUserParameters(ZipOutputStream zipStream) throws IOException, TransformerConfigurationException, SAXException {
        if (isCanceled()) return;
        logger.info("Saving user parameters");
        zipStream.putNextEntry(new ZipEntry("User parameters.xml"));
        userParameterSaveHandler = new UserParameterSaveHandler(zipStream, savedProject, dataFilesIDMap);
        currentSavedObjectName = "User parameters";
        userParameterSaveHandler.saveParameters();
    }
