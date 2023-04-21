    public void loadConfig(String filename) {
        File file = new File(filename);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(file);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "<html> Config File not found<br>", "Config File Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) {
                x = sxe.getException();
            }
            x.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Element configData = document.getDocumentElement();
        if (configData == null) {
            System.out.println("ups!!");
            return;
        }
        internetAddrInput.setText(configData.getAttribute("TCPIPHost").trim());
        portInput.setText(configData.getAttribute("TCPIPPort").trim());
        evolators.setSelectedIndex(Integer.parseInt(configData.getAttribute("EvolutionProgramm").trim()));
        fitnessFunctions.setSelectedItem(configData.getAttribute("FitnessFunction").trim());
        try {
            simulators.setSelectedIndex(Integer.parseInt(configData.getAttribute("Simulator").trim()));
            System.out.println("This is an old version of config file types." + " No Problem, it can be read, but please rewrite it soon!");
        } catch (NumberFormatException ex) {
            simulators.setSelectedItem(configData.getAttribute("Simulator").trim());
        }
        simInternetAddrInput.setText(configData.getAttribute("SimulatorIP").trim());
        simPortInput.setText(configData.getAttribute("SimulatorPort").trim());
        processParameter.setMaxSpeed(Double.parseDouble(configData.getAttribute("SpeedFactor").trim()));
        processParameter.setCycles(Integer.parseInt(configData.getAttribute("Cycles").trim()));
        processParameter.setIterations(Integer.parseInt(configData.getAttribute("Iterations").trim()));
        if (configData.getAttribute("InitialIterations") != null) {
            processParameter.setInitialIterations(Integer.parseInt(configData.getAttribute("InitialIterations").trim()));
        }
        processParameter.setTries(Integer.parseInt(configData.getAttribute("Tries").trim()));
        processParameter.setWarmUpSteps(Integer.parseInt(configData.getAttribute("WarmUpSteps").trim()));
        processParameter.setConstants(Double.parseDouble(configData.getAttribute("Constants0").trim()), Double.parseDouble(configData.getAttribute("Constants1").trim()), Double.parseDouble(configData.getAttribute("Constants2").trim()), Double.parseDouble(configData.getAttribute("Constants3").trim()));
        processParameter.setDisplay((Integer.parseInt(configData.getAttribute("Display").trim()) == 0) ? false : true);
        processParameterDialog.updatePanel();
    }
