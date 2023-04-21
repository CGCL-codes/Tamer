    protected void initComponents() {
        final Dimension windowSize = new Dimension(1020, 500);
        setSize(windowSize);
        mainPanel = getContentPane();
        mainPanel.setSize(windowSize);
        screenConsole = new Box(VERTICAL);
        Box screenRowA = new Box(HORIZONTAL);
        screenConsole.add(screenRowA);
        screenConsole.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel screenContainer = new JPanel();
        EdgeLayout screenLayout = new EdgeLayout();
        screenContainer.setLayout(screenLayout);
        scopeScreen = new ScopeScreen(scopeModel);
        scopeScreen.setBorder(new BevelBorder(BevelBorder.LOWERED));
        screenLayout.add(scopeScreen, screenContainer, 0, 0, EdgeLayout.ALL_SIDES, EdgeLayout.GROW_BOTH);
        screenRowA.add(screenContainer);
        Box rightButtonPanel = new Box(VERTICAL);
        screenRowA.add(rightButtonPanel);
        Dimension buttonSize = new JButton("TTTTTT").getPreferredSize();
        gridButton = new JToggleButton("Grid");
        gridButton.setMinimumSize(buttonSize);
        gridButton.setMaximumSize(buttonSize);
        gridButton.setToolTipText("Toggle the grid.");
        gridButton.setSelected(scopeScreen.isGridVisible());
        addButtonToVertical(gridButton, rightButtonPanel);
        gridButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                scopeScreen.toggleGridVisible();
                updateView();
            }
        });
        legendButton = new JToggleButton("Legend");
        legendButton.setMinimumSize(buttonSize);
        legendButton.setMaximumSize(buttonSize);
        legendButton.setToolTipText("Toggle the legend.");
        legendButton.setSelected(scopeScreen.isGridVisible());
        addButtonToVertical(legendButton, rightButtonPanel);
        legendButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                scopeScreen.toggleLegendVisible();
                updateView();
            }
        });
        JButton pngCaptureButton = new JButton("");
        pngCaptureButton.setAction(ActionFactory.captureWindowAsImageAction(this.document));
        pngCaptureButton.setText(null);
        pngCaptureButton.setMinimumSize(buttonSize);
        pngCaptureButton.setMaximumSize(buttonSize);
        pngCaptureButton.setToolTipText("Capture the scope window to a PNG image file.");
        addButtonToVertical(pngCaptureButton, rightButtonPanel);
        refreshRateButton = new JButton("Rate");
        refreshRateButton.setMinimumSize(buttonSize);
        refreshRateButton.setMaximumSize(buttonSize);
        refreshRateButton.setToolTipText("Set the refresh rate.");
        addButtonToVertical(refreshRateButton, rightButtonPanel);
        refreshRateButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                RefreshRateDialog.show(MainWindow.this, scopeModel);
            }
        });
        JButton dataCaptureButton = new JButton("");
        dataCaptureButton.setMinimumSize(buttonSize);
        dataCaptureButton.setMaximumSize(buttonSize);
        dataCaptureButton.setIcon(IconLib.getIcon(IconGroup.GENERAL, "Export24.gif"));
        dataCaptureButton.setToolTipText("Dump raw waveform data to a text file.");
        dataCaptureButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                    WaveformOutputHandler.writeRawWaveformSnapshot(MainWindow.this, scopeModel);
                } catch (java.io.IOException exception) {
                    displayError("Error writing data", "Error attempting to write out waveform data.", exception);
                    System.err.println(exception);
                } catch (RuntimeException exception) {
                    displayError("Error writing data", "Error attempting to write out waveform data.", exception);
                    System.err.println(exception);
                }
            }
        });
        addButtonToVertical(dataCaptureButton, rightButtonPanel);
        JButton findButton = new JButton("");
        findButton.setMinimumSize(buttonSize);
        findButton.setMaximumSize(buttonSize);
        findButton.setIcon(IconLib.getIcon(IconGroup.GENERAL, "Find24.gif"));
        findButton.setToolTipText("Find the waveforms.");
        findButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                try {
                    scopeScreen.findWaveforms();
                } catch (RuntimeException exception) {
                    displayError("Error finding the waveforms", "Error attempting to find the waveforms.", exception);
                    System.err.println(exception);
                }
            }
        });
        addButtonToVertical(findButton, rightButtonPanel);
        fftHandlerButton = new JToggleButton("FFT");
        fftHandlerButton.setMinimumSize(buttonSize);
        fftHandlerButton.setMaximumSize(buttonSize);
        fftHandlerButton.setToolTipText("Toggle between FFT and default screen.");
        fftHandlerButton.addActionListener(new AbstractAction("FFT") {

            public void actionPerformed(ActionEvent event) {
                scopeScreen.toggleFFTHandler();
                updateView();
            }
        });
        addButtonToVertical(fftHandlerButton, rightButtonPanel);
        rightButtonPanel.add(Box.createVerticalGlue());
        setupButton = new JToggleButton("Configure");
        setupButton.setMargin(new Insets(1, 1, 1, 1));
        setupButton.setMinimumSize(buttonSize);
        setupButton.setMaximumSize(buttonSize);
        setupButton.setToolTipText("Reveal or hide the settings panel.");
        setupButton.setSelected(true);
        setupButton.addActionListener(new AbstractAction("Setup") {

            public void actionPerformed(ActionEvent event) {
                if (setupButton.isSelected()) {
                    mainSplitView.setDividerLocation(-1);
                } else {
                    mainSplitView.setDividerLocation(1.0);
                }
                updateView();
            }
        });
        addButtonToVertical(setupButton, rightButtonPanel);
        JPanel bottomButtonPanel = new JPanel();
        EdgeLayout bottomButtonLayout = new EdgeLayout();
        bottomButtonPanel.setPreferredSize(new Dimension(500, 40));
        bottomButtonPanel.setMaximumSize(new Dimension(10000, 40));
        bottomButtonPanel.setLayout(bottomButtonLayout);
        screenConsole.add(bottomButtonPanel);
        brightnessSlider = new JSlider(0, 100, (int) (100 * scopeScreen.getBrightness()));
        brightnessSlider.setToolTipText("Adjust the background brightness.");
        Dictionary brightnessLabelTable = new Hashtable();
        brightnessLabelTable.put(new Integer(0), new JLabel("Dark"));
        brightnessLabelTable.put(new Integer(100), new JLabel("Light"));
        brightnessSlider.setLabelTable(brightnessLabelTable);
        brightnessSlider.setPaintLabels(true);
        brightnessSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent evt) {
                float brightness = ((float) (brightnessSlider.getValue())) / 100;
                scopeScreen.setBrightness(brightness);
            }
        });
        bottomButtonLayout.setConstraints(brightnessSlider, 10, 5, 0, 0, EdgeLayout.TOP_LEFT);
        bottomButtonPanel.add(brightnessSlider);
        Box controlPanel = new Box(HORIZONTAL);
        controlPanel.setPreferredSize(new Dimension(400, 350));
        JScrollPane controlScrollPane = new JScrollPane(controlPanel);
        controlScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        controlScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        SummaryView summaryView = new SummaryView(scopeModel);
        JScrollPane summaryScrollPane = new JScrollPane(summaryView);
        summaryScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        summaryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        Box upperView = new Box(VERTICAL);
        upperView.add(controlScrollPane);
        upperView.add(Box.createVerticalGlue());
        JSplitPane controlSplitView = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, upperView, summaryScrollPane);
        controlSplitView.setResizeWeight(1.0);
        controlSplitView.setOneTouchExpandable(true);
        mainSplitView = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, screenConsole, controlSplitView);
        mainSplitView.setResizeWeight(1.0);
        mainSplitView.setEnabled(false);
        mainSplitView.setDividerSize(1);
        mainSplitView.resetToPreferredSizes();
        mainPanel.add(mainSplitView);
        channelSelectorPanel = new Box(VERTICAL);
        channelSelectorPanel.setBorder(new TitledBorder(new EtchedBorder(), "Channels"));
        JToggleButton selectedChannelButton = createChannelSelectors();
        channelSelectorPanel.setMinimumSize(channelSelectorPanel.getPreferredSize());
        channelSelectorPanel.setMaximumSize(channelSelectorPanel.getPreferredSize());
        Box selectorBox = new Box(VERTICAL);
        selectorBox.add(channelSelectorPanel);
        selectorBox.add(Box.createVerticalGlue());
        controlPanel.add(selectorBox);
        settingsPanel = new Box(VERTICAL);
        controlPanel.add(settingsPanel);
        Dimension settingPanelSize = new Dimension(300, 350);
        settingsPanel.setPreferredSize(settingPanelSize);
        channelPanel = new ChannelPanel();
        settingsPanel.add(channelPanel);
        ChannelModel channelModel = scopeModel.getChannelModel(0);
        selectedChannelButton.doClick();
        mathPanel = new MathPanel();
        timeBasePanel = new TimeBasePanel(scopeScreen.getDefaultScreenHandler().getTimeDisplaySettings(), scopeModel.getTimeModel());
        triggerPanel = new TriggerPanel(scopeModel.getTrigger());
        updateView();
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent event) {
                channelPanel.resetDefaultFocus();
            }
        });
    }
