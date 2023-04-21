    public void createOutputFiles() throws IOException {
        final String destination = ConfigurationOptions.getOutputDir() + getLink();
        logger.info("Copying CSS file to '" + destination + "'");
        FileUtils.copyFile(filename, destination);
    }
