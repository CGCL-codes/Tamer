    private void copyFromUndoDir(String name, String propertiesBackup, Scenario scenario) throws Exception {
        FileUtils.copyFile(new File(scenariosUndoRedoDirectory, name), scenario.getScenarioFile());
        File propertiesFile = new File(ScenarioHelpers.getScenarioPropertiesFile(scenario.getName()));
        File backPropertiesFile = new File(scenariosUndoRedoDirectory, propertiesBackup);
        if (backPropertiesFile.exists()) {
            FileUtils.copyFile(backPropertiesFile, propertiesFile);
        }
    }
