    @Before
    public void before() throws Exception {
        super.before();
        scenarioFolder = new File(testbed, "scenarios");
        System.out.println("Preparing environment");
        baseScenarioXml = new File(scenarioFolder, SCENARIO_NAME_XML + ".backup");
        new File(scenarioFolder, SCENARIO_NAME_XML).renameTo(baseScenarioXml);
        baseScenarioProperties = new File(scenarioFolder, SCENARIO_NAME_PROP + ".backup");
        new File(scenarioFolder, SCENARIO_NAME_PROP).renameTo(baseScenarioProperties);
        for (int i = 0; i < NUM_OF_SCENARIOS; i++) {
            File tempXml = new File(scenarioFolder, SCENARIO_NAME_XML);
            FileUtils.copyFile(baseScenarioXml, tempXml);
            ScenarioXMLFile xmlFile = new ScenarioXMLFile(tempXml);
            xmlFile.rename("scenarios/" + SCENARIO_NAME_XML.replaceAll("(Performance).xml", "$1" + i));
            xmlFile.save();
            File tempProp = new File(scenarioFolder, SCENARIO_NAME_PROP);
            FileUtils.copyFile(baseScenarioProperties, tempProp);
            ScenarioPropertiesFile propFile = new ScenarioPropertiesFile(tempProp);
            propFile.rename("scenarios/" + SCENARIO_NAME_PROP.replaceAll("(Performance).properties", "$1" + i));
            propFile.save();
        }
        System.out.println("Finished preparing environment");
    }
