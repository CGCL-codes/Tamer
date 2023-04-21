    private void saveScenarioToUndoDir(Scenario scenario, String newName, String propertiesBackupName) throws Exception {
        FileUtils.copyFile(scenario.getScenarioFile(), new File(scenariosUndoRedoDirectory, newName));
        File propertiesFile = new File(ScenarioHelpers.getScenarioPropertiesFile(scenario.getName()));
        if (propertiesFile.exists()) {
            FileUtils.copyFile(propertiesFile, new File(scenariosUndoRedoDirectory, propertiesBackupName));
        }
    }
