    public void testCreator() {
        try {
            FileUtils.copyFile(modelLoadPath, modelPath);
            creator = new ScenarioCreator(modelPath, scenarioPath, true);
            creator.addExtension(new DataLoaderScenarioCreatorExtension());
            creator.createScenario();
            checkResults(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }
