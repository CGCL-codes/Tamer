    private void initiate() {
        SimpleChartPopupMenu.addPopupMenuTo(GP);
        GP.setOffScreenImageDrawing(true);
        GP.setGraphBackGroundColor(Color.white);
        GP.setName("Wires' Signals. File: ");
        GP.setAxisNames("sample index", "signal, a.u.");
        GP.setNumberFormatX(new DecimalFormat("##0"));
        GP.setNumberFormatY(new DecimalFormat("0.0#"));
        GP.setLimitsAndTicksX(0., 50., 4, 4);
        GP.setLimitsAndTicksY(-1.0, 0.5, 4, 4);
        readRawDataPanel = new ReadRawDataPanel(rawData);
        plotRawDataPanel = new PlotRawDataPanel(rawData, GP);
        filterRawDataPanel = new FilterRawDataPanel(GP);
        makeRawToEmittancePanel = new MakeRawToEmittancePanel(GP);
        rawData.setMessageTextField(messageTextLocal);
        makeRawToEmittancePanel.setMessageTextField(messageTextLocal);
        makeRawToEmittancePanel.setWireRawData(rawData);
        makeRawToEmittancePanel.setFilterRawDataPanel(filterRawDataPanel);
        makeRawToEmittancePanel.setPlotRawDataPanel(plotRawDataPanel);
        Border etchedBorder = BorderFactory.createEtchedBorder();
        border = BorderFactory.createTitledBorder(etchedBorder, "RAW DATA PANEL");
        panel.setBorder(border);
        panel.setLayout(new BorderLayout());
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new VerticalLayout());
        leftPanel.add(readRawDataPanel.getJPanel());
        leftPanel.add(plotRawDataPanel.getJPanel());
        leftPanel.add(filterRawDataPanel.getJPanel());
        JPanel graphPanel = new JPanel();
        graphPanel.setBorder(etchedBorder);
        graphPanel.setLayout(new BorderLayout());
        graphPanel.add(GP, BorderLayout.CENTER);
        JPanel upperPanel = new JPanel();
        upperPanel.setBorder(etchedBorder);
        upperPanel.setLayout(new BorderLayout());
        upperPanel.add(leftPanel, BorderLayout.WEST);
        upperPanel.add(graphPanel, BorderLayout.CENTER);
        panel.add(upperPanel, BorderLayout.NORTH);
        panel.add(makeRawToEmittancePanel.getJPanel(), BorderLayout.CENTER);
        iniAnalysisEvent = new ActionEvent(this, 0, "READ_NEW_DATA");
        readRawDataPanel.addReadDataActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                GP.setName("Wires' Signals. File: " + readRawDataPanel.getFileName());
                raw_data_file_name = readRawDataPanel.getFileName();
                int nSamples = rawData.getSamplesNumber();
                int samplesStep = nSamples / 50;
                if ((nSamples % 50) > 0) {
                    samplesStep++;
                }
                GP.setLimitsAndTicksX(0., 50., samplesStep, 4);
                plotRawDataPanel.setSpinnerModels(rawData.getChannelsNumber(), rawData.getPositionsNumberSlit(), rawData.getPositionsNumberHarp());
                plotRawDataPanel.setDefaultSpinnersValues();
                plotRawDataPanel.plotRawData();
                if (initializationAnalysisListener != null) {
                    initializationAnalysisListener.actionPerformed(iniAnalysisEvent);
                }
                makeRawToEmittancePanel.initAfterReading();
            }
        });
        readRawDataPanel.addSetIndexActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                messageTextLocal.setText(null);
                ReadRawDataPanel rdp = (ReadRawDataPanel) e.getSource();
                int index = rdp.getTypeIndex();
                makeRawToEmittancePanel.setTypeDataIndex(index);
            }
        });
        setFontForAll(new Font("Monospaced", Font.PLAIN, 10));
        readRawDataPanel.setTypeIndex(3);
    }
