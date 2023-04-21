    public static void main(String[] args) {
        argumentHandling(args);
        if (turingMachinePath.isEmpty()) usage("The TuringMachine´s filename is missing.", EXIT_FAILURE);
        if (printPropertiesFlag) {
            readProperties(printPropertiesValue);
        }
        if (maxStepsFlag) {
            try {
                defaultSteps = Integer.parseInt(maxStepsValue);
            } catch (NumberFormatException ex) {
                logger.log(Level.WARNING, "Given Maximumstep´s Optionvalue is not an Integer. -> reset to 50", ex);
                defaultSteps = 50;
            }
        }
        if (reportStyleOffFlag) logger.setLevel(Level.OFF);
        logger.info("########################## TuringMachine Simulator ##########################");
        TuringMachine turingMachine = readTuringMachine();
        logger.info("Loaded TuringMachine:");
        turingMachine.printTuringMachine(logger, Level.INFO, turingMachinePrintFormat, transitionCollectionPrintFormat, collectionRowPrintFormat, collectionRowElementPrintFormat, baseConfigurationPrintFormat, statePrintFormat);
        if (!skipValidationFlag) {
            try {
                turingMachine.validate();
            } catch (TuringMachineValidationException ex) {
                if (reportStyleOffFlag) logger.setLevel(Level.ALL);
                logger.log(Level.WARNING, "loadTuringMachineXML(File file): Validation of loaded TuringMachine failed.");
                ArrayList<Integer> flags = ex.getCheckFailed();
                for (int flag : flags) {
                    logger.log(Level.WARNING, ex.getMessageForFlag(flag));
                }
                if (reportStyleOffFlag) logger.setLevel(Level.OFF);
            }
        }
        if (testRunFlag) {
            try {
                File testRunFile = new File(testRunValue);
                ArrayList<TuringMachineTestConfiguration> testRuns = XML_IO.readTMTestConfigFromXML(testRunFile);
                for (int i = 0; i < testRuns.size(); i++) {
                    turingMachine.setBaseConfiguration(testRuns.get(i).getBaseConfiguration());
                    if (!maxStepsFlag) defaultSteps = testRuns.get(i).getMaxSteps();
                    printMandatoryInformation("###### Simulation No. " + (i + 1) + " ######");
                    runSimulation(turingMachine, testRuns.get(i).isTuringMachineSuccess());
                }
            } catch (ParserConfigurationException ex) {
                writeErrorAndExit("An Error occured while parsing the TestRuns XML-Definition file.", ex);
            } catch (SAXException ex) {
                writeErrorAndExit("An Error occured while parsing the TestRuns XML-Definition file.", ex);
            } catch (IOException ex) {
                writeErrorAndExit("An IO Error occured while trying to read the TestRuns XML-Definiton file.", ex);
            } catch (TapeOutofBoundsException ex) {
                writeErrorAndExit("One of the TestRuns seems to be incorrectly configured, one of the TapeHead´s position is outside of it´s Tape´s bounds.", ex);
            }
        } else {
            runSimulation(turingMachine, true);
        }
        System.exit(program_exitcode);
    }
