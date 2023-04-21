    public CsvVersion generateEmptyCsvFiles(MappingDescriptor mappingDescriptor, File basePath) throws BundledException {
        try {
            LOGGER.info("Starting the CSV files generation .....");
            CsvVersion newVersion = getCsvVersionManager().getNewVersion(mappingDescriptor, basePath);
            String workingFileName = getCsvVersionManager().format(newVersion);
            LOGGER.info("Version of file to process : " + workingFileName);
            for (FolderDescriptor folder : mappingDescriptor.getFolders()) {
                LOGGER.info("Processing the folder named " + folder.getFolderName() + " ......");
                List<String> csvColumns = getCsvBackgroundProcess().getColumnNames(folder);
                List<String> csvComments = getCsvBackgroundProcess().getNeededComments(folder);
                final File folderPath = new File(basePath, folder.getFolderName());
                folderPath.mkdirs();
                File pathToFile = new File(folderPath, workingFileName);
                if (csvComments.isEmpty()) {
                    getCsvUtils().writeCsvHeaders(pathToFile, csvColumns);
                } else {
                    getCsvUtils().writeCsvHeaders(pathToFile, csvColumns, csvComments);
                }
                LOGGER.info("Folder named " + folder.getFolderName() + " processed successfully !");
            }
            LOGGER.info("Writing the readme file ....");
            List<String> readmeTexts = getCsvBackgroundProcess().getReadmeDependencies(mappingDescriptor);
            getCsvUtils().writeReadmeTexts(new File(basePath, README_FILENAME), readmeTexts);
            LOGGER.info("CSV files generation finished successfully !");
            return newVersion;
        } catch (BundledException error) {
            LOGGER.error(getMessageSource().getMessage(error.getI18nMessage(), Locale.getDefault()), error);
            throw error;
        }
    }
