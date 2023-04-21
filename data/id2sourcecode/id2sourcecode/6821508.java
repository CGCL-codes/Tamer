    private void copyScoreFile() throws IOException {
        if (!modelPath.getParentFile().equals(scenarioPath)) {
            FileUtils.copyFile(modelPath, new File(scenarioPath, ScenarioConstants.LEGACY_SCORE_FILE_NAME));
        }
    }
