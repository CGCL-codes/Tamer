    public void createOutputFiles() throws IOException {
        final String destination = ConfigurationOptions.getOutputDir() + filename;
        logger.info("Creating CSS file at '" + destination + "'");
        FileUtils.copyFile(Main.class.getResourceAsStream(ReportSuiteMaker.WEB_FILE_PATH + filename), new File(destination));
    }
