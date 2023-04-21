    private static TuringMachine readTuringMachine() {
        TuringMachine turingMachine = null;
        File tmFile = new File(turingMachinePath);
        if (tmFile.getName().endsWith(".tmx")) {
            try {
                turingMachine = TuringMachine.loadTuringMachineXML(tmFile);
            } catch (ParserConfigurationException ex) {
                writeErrorAndExit("An Error occured while parsing the TuringMachine XML-Definition file.", ex);
            } catch (SAXException ex) {
                writeErrorAndExit("An Error occured while parsing the TuringMachine XML-Definition file.", ex);
            } catch (IOException ex) {
                writeErrorAndExit("An IO Error occured while trying to read the TuringMachine XML-Definiton file.", ex);
            }
        } else {
            try {
                turingMachine = TuringMachine.loadTuringMachine(tmFile);
            } catch (FileNotFoundException ex) {
                writeErrorAndExit("An IO Error occured, the TuringMachine JavaObject file was not found.", ex);
            } catch (IOException ex) {
                writeErrorAndExit("An IO Error occured while trying to read the TuringMachine JavaObject file.", ex);
            } catch (ClassNotFoundException ex) {
                writeErrorAndExit("THIS ERROR SHOULD NOT HAPPEN. An Error occured while calling the java.io.ObjectInputStream.class readObject() function.", ex);
            }
        }
        return turingMachine;
    }
