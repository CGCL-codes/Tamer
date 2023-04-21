    public void testJPFStyleCreator() {
        try {
            FileUtils.copyFile(modelLoadPath, modelPath);
            creator = new ScenarioCreator(modelPath, scenarioPath, true);
            Boot boot = new Boot();
            PluginManager manager = boot.init(new String[] { "" });
            ScenarioCreatorExtensions ext = new ScenarioCreatorExtensions();
            ext.loadExtensions(manager);
            creator = new ScenarioCreator(modelPath, scenarioPath, true);
            creator.createScenario();
            checkResults(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
