    private void saveToSrcDirectory() throws Exception {
        File scenarioSrcFile = getSourceScenarioFile();
        if (scenarioSrcFile == null) {
            log.log(Level.SEVERE, "fail to write scenario to src directory, directory is not set in jsystem.properties or does not exist");
            return;
        }
        scenarioSrcFile.getParentFile().mkdirs();
        try {
            FileUtils.copyFile(scenarioFile, scenarioSrcFile);
        } catch (Exception e) {
            log.log(Level.SEVERE, "fail to write scenario to src directory", e);
        }
    }
