    public void setDataFromXML(XmlDataAdaptor rawDataPanelData) {
        XmlDataAdaptor makeRawToEmittPanelData = rawDataPanelData.childAdaptor(getNameXMLData());
        XmlDataAdaptor params = makeRawToEmittPanelData.childAdaptor("PARAMS");
        useFilter_Button.setSelected(params.booleanValue("useFilter"));
        useGraphData_Button.setSelected(params.booleanValue("useGraphData"));
        distHS_Text.setValue(params.doubleValue("distHS"));
        energy_Text.setValue(params.doubleValue("energy"));
        distWW_Text.setValue(params.doubleValue("distWW"));
        gaussAmp_Text.setValue(params.doubleValue("gaussAmp"));
        gaussWidth_Text.setValue(params.doubleValue("gaussWidth"));
        threshold_Text.setValue(params.doubleValue("threshold"));
        emScrResX_Spinner.setValue(new Integer(params.intValue("emScrResX")));
        emScrResY_Spinner.setValue(new Integer(params.intValue("emScrResY")));
        emSizeX_Spinner.setValue(new Integer(params.intValue("emSizeX")));
        emSizeY_Spinner.setValue(new Integer(params.intValue("emSizeY")));
        debug_gauss_generation = false;
        XmlDataAdaptor debug_gauss_genD = makeRawToEmittPanelData.childAdaptor("GAUSS_GENERATOR");
        if (debug_gauss_genD != null) {
            debug_gauss_generation = true;
        }
        wireSignalData.setDataFromXML(makeRawToEmittPanelData);
        setGridLimitsGP_sp(wireSignalData.getChannelsNumber(), wireSignalData.getPositionsNumberSlit());
        useHarpPos_Spinner.setModel(new SpinnerNumberModel(1, 1, wireSignalData.getPositionsNumberHarp(), 1));
        plotWiresSignalsData();
        makeEmittanceData();
        plotEmittanceData();
    }
