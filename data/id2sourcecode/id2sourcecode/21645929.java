    public static void main(final String[] args) {
        if (args.length != 5) {
            printUsage();
            return;
        }
        String inputPopulationFilename = args[0];
        String outputPopulationFilename = args[1];
        String zonesFilename = args[2];
        String inputMatricesDirname = args[3];
        String outputMatricesDirname = args[4];
        if (inputPopulationFilename.equals(outputPopulationFilename)) {
            System.err.println("Input and Output population file must be different.");
            return;
        }
        File inputPopulationFile = new File(inputPopulationFilename);
        File outputPopulationFile = new File(outputPopulationFilename);
        File zonesFile = new File(zonesFilename);
        if (!inputPopulationFile.exists()) {
            System.err.println("Input population file does not exist.");
            return;
        }
        if (outputPopulationFile.exists()) {
            System.err.println("Output population file already exists. Will NOT overwrite it. Aborting.");
            return;
        }
        if (!zonesFile.exists()) {
            System.err.println("zones file does not exist.");
            return;
        }
        ShapeFileReader shpReader = new ShapeFileReader();
        shpReader.readFileAndInitialize(zonesFilename);
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        PopulationImpl pop = (PopulationImpl) scenario.getPopulation();
        pop.setIsStreaming(true);
        new MatsimPopulationReader(scenario).parse(inputPopulationFilename);
        System.out.println("All done.");
    }
