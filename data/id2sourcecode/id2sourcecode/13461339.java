    @TestProperties(name = "5.2.2.4 test refresh functionlity")
    public void testRefresh() throws Exception {
        jsystem.launch();
        int numOfTests = 4;
        String refreshScenarioName = "refreshScenario";
        report.step("create a scenario with few steps");
        jsystem.createScenario(refreshScenarioName);
        jsystem.addTest("testShouldPass", "GenericBasic", numOfTests);
        jsystem.saveScenario();
        report.step("backup the scenario");
        final File refreshScenarioXml = new File(jsystem.getJSystemProperty(FrameworkOptions.TESTS_CLASS_FOLDER) + File.separator + "scenarios" + File.separator + refreshScenarioName + ".xml");
        Assert.assertTrue("Scenario file was not found", refreshScenarioXml.exists());
        final File refreshScenarioProp = new File(jsystem.getJSystemProperty(FrameworkOptions.TESTS_CLASS_FOLDER) + File.separator + "scenarios" + File.separator + refreshScenarioName + ".properties");
        Assert.assertTrue("Scenario file was not found", refreshScenarioProp.exists());
        File tempScenarioXml = new File(refreshScenarioXml.getParent(), "tempScen.xml");
        tempScenarioXml.delete();
        FileUtils.copyFile(refreshScenarioXml, tempScenarioXml);
        Assert.assertTrue("Temp Scenario file was not found", tempScenarioXml.exists());
        File tempScenarioProp = new File(refreshScenarioXml.getParent(), "tempScen.properties");
        tempScenarioProp.delete();
        FileUtils.copyFile(refreshScenarioProp, tempScenarioProp);
        Assert.assertTrue("Temp Scenario file was not found", tempScenarioProp.exists());
        report.step("clear the scenario");
        String scenarioName = jsystem.getCurrentScenario();
        ScenarioUtils.createAndCleanScenario(jsystem, scenarioName);
        jsystem.createScenario(refreshScenarioName);
        jsystem.saveScenario();
        if (!refreshScenarioXml.delete() || !refreshScenarioProp.delete()) {
            throw new IOException("Failed to delete scenario " + refreshScenarioName);
        }
        report.step("restore the scenario");
        FileUtils.copyFile(tempScenarioXml, refreshScenarioXml);
        FileUtils.copyFile(tempScenarioProp, refreshScenarioProp);
        report.step("refresh and run the scenario");
        jsystem.refresh();
        report.step("check that the restored scenario was executed");
        jsystem.checkNumberOfTestsExistInScenario(refreshScenarioName, numOfTests);
        tempScenarioXml.delete();
        tempScenarioProp.delete();
    }
