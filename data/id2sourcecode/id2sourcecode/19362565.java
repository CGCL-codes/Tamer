    private static void readProperties(String propertiesName) {
        FileInputStream fileStream = null;
        try {
            Properties properties = new Properties();
            fileStream = new FileInputStream(propertiesName);
            properties.load(fileStream);
            turingMachinePrintFormat = properties.getProperty("turingMachinePrintFormat", turingMachinePrintFormat);
            transitionCollectionPrintFormat = properties.getProperty("transitionCollectionPrintFormat", transitionCollectionPrintFormat);
            collectionRowElementPrintFormat = properties.getProperty("collectionRowElementPrintFormat", collectionRowElementPrintFormat);
            baseConfigurationPrintFormat = properties.getProperty("baseConfigurationPrintFormat", baseConfigurationPrintFormat);
            statePrintFormat = properties.getProperty("statePrintFormat", statePrintFormat);
            transitionPrintFormat = properties.getProperty("transitionPrintFormat", transitionPrintFormat);
            stepPrintFormat = properties.getProperty("stepPrintFormat", stepPrintFormat);
        } catch (FileNotFoundException ex) {
            writeErrorWithoutExit("An IO Error occured, the print.properties file was not found. The Programm continues with the printFormat defaults.", ex);
        } catch (IOException ex) {
            writeErrorWithoutExit("An IO Error occured while trying to read the print.properties file. The Programm continues with the printFormat defaults.", ex);
        }
    }
