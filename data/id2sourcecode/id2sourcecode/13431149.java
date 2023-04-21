    public static void copyScenarioPropertiesFileToNewScenario(String oldScenarioName, String newScenarioName) throws IOException {
        String oldPropertiesFilePath = getScenarioSrcPropertiesFile(oldScenarioName);
        if (!new File(oldPropertiesFilePath).exists()) {
            return;
        }
        String newPropertiesFilePath = getScenarioSrcPropertiesFile(newScenarioName);
        FileUtils.copyFile(oldPropertiesFilePath, newPropertiesFilePath);
        String newPropertiesFilePathClasses = getScenarioPropertiesFile(newScenarioName);
        FileUtils.copyFile(newPropertiesFilePath, newPropertiesFilePathClasses);
    }
