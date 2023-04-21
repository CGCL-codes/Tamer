    public void testExternal() {
        try {
            FileUtils.copyFile(modelExternalPath, modelPath);
            creator = new ScenarioCreator(modelPath, scenarioPath, true);
            creator.addExtension(new DataLoaderScenarioCreatorExtension());
            creator.createScenario();
            checkResults(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }
