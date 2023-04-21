    public static String backupDefaultScenarioFile() throws IOException {
        String defaultName = "scenarios" + File.separator + "default";
        long time = System.currentTimeMillis();
        String newName = defaultName + "_" + time;
        File scenarioDirectoryFile = ScenariosManager.getInstance().getScenariosDirectoryFiles();
        String defaultClassFile = new File(scenarioDirectoryFile, defaultName + ".xml").getAbsolutePath();
        String scenariosSrc = JSystemProperties.getInstance().getPreference(FrameworkOptions.TESTS_SOURCE_FOLDER);
        copyScenarioPropertiesFileToNewScenario(defaultName, newName);
        FileUtils.copyFile(defaultClassFile, new File(scenariosSrc, newName + ".xml").getAbsolutePath());
        FileUtils.copyFile(defaultClassFile, new File(scenarioDirectoryFile, newName + ".xml").getAbsolutePath());
        FileUtils.deleteFile(new File(scenariosSrc, defaultName + ".xml").getAbsolutePath());
        FileUtils.deleteFile(defaultClassFile);
        return newName;
    }
