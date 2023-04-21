    public AreaViewer(Simulation simulationToVisualize, GUI gui) {
        super("MRM - Area Viewer", gui);
        currentSimulation = simulationToVisualize;
        currentRadioMedium = (MRM) currentSimulation.getRadioMedium();
        currentChannelModel = currentRadioMedium.getChannelModel();
        currentChannelModel.addSettingsObserver(channelModelSettingsObserver);
        currentRadioMedium.addSettingsObserver(radioMediumSettingsObserver);
        currentRadioMedium.addRadioMediumObserver(radioMediumActivityObserver);
        setSize(500, 500);
        setVisible(true);
        thisPlugin = this;
        showSettingsBox = new JCheckBox("settings", true);
        showSettingsBox.setAlignmentY(Component.TOP_ALIGNMENT);
        showSettingsBox.setContentAreaFilled(false);
        showSettingsBox.setActionCommand("toggle show settings");
        showSettingsBox.addActionListener(canvasModeHandler);
        JRadioButton selectModeButton = new JRadioButton("select");
        selectModeButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        selectModeButton.setContentAreaFilled(false);
        selectModeButton.setActionCommand("set select mode");
        selectModeButton.addActionListener(canvasModeHandler);
        selectModeButton.setSelected(true);
        JRadioButton panModeButton = new JRadioButton("pan");
        panModeButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        panModeButton.setContentAreaFilled(false);
        panModeButton.setActionCommand("set pan mode");
        panModeButton.addActionListener(canvasModeHandler);
        JRadioButton zoomModeButton = new JRadioButton("zoom");
        zoomModeButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        zoomModeButton.setContentAreaFilled(false);
        zoomModeButton.setActionCommand("set zoom mode");
        zoomModeButton.addActionListener(canvasModeHandler);
        JRadioButton trackModeButton = new JRadioButton("track rays");
        trackModeButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        trackModeButton.setContentAreaFilled(false);
        trackModeButton.setActionCommand("set track rays mode");
        trackModeButton.addActionListener(canvasModeHandler);
        ButtonGroup group = new ButtonGroup();
        group.add(selectModeButton);
        group.add(panModeButton);
        group.add(zoomModeButton);
        group.add(trackModeButton);
        canvas = new JPanel() {

            private static final long serialVersionUID = 1L;

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                repaintCanvas((Graphics2D) g);
            }
        };
        canvas.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        canvas.setBackground(Color.WHITE);
        canvas.setLayout(new BorderLayout());
        canvas.addMouseListener(canvasMouseHandler);
        JPanel canvasModePanel = new JPanel();
        canvasModePanel.setOpaque(false);
        canvasModePanel.setLayout(new BoxLayout(canvasModePanel, BoxLayout.Y_AXIS));
        canvasModePanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        canvasModePanel.add(showSettingsBox);
        canvasModePanel.add(Box.createVerticalGlue());
        canvasModePanel.add(selectModeButton);
        canvasModePanel.add(panModeButton);
        canvasModePanel.add(zoomModeButton);
        canvasModePanel.add(trackModeButton);
        canvas.add(BorderLayout.EAST, canvasModePanel);
        JPanel graphicsComponentsPanel = new JPanel();
        graphicsComponentsPanel.setLayout(new BoxLayout(graphicsComponentsPanel, BoxLayout.Y_AXIS));
        graphicsComponentsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        graphicsComponentsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        graphicsComponentsPanel.add(new JLabel("Show/Hide:"));
        backgroundCheckBox = new JCheckBox("Background image", true);
        backgroundCheckBox.setActionCommand("toggle background");
        backgroundCheckBox.addActionListener(selectGraphicsHandler);
        graphicsComponentsPanel.add(backgroundCheckBox);
        obstaclesCheckBox = new JCheckBox("Obstacles", true);
        obstaclesCheckBox.setActionCommand("toggle obstacles");
        obstaclesCheckBox.addActionListener(selectGraphicsHandler);
        graphicsComponentsPanel.add(obstaclesCheckBox);
        channelCheckBox = new JCheckBox("Channel", true);
        channelCheckBox.setActionCommand("toggle channel");
        channelCheckBox.addActionListener(selectGraphicsHandler);
        graphicsComponentsPanel.add(channelCheckBox);
        radiosCheckBox = new JCheckBox("Radios", true);
        radiosCheckBox.setActionCommand("toggle radios");
        radiosCheckBox.addActionListener(selectGraphicsHandler);
        graphicsComponentsPanel.add(radiosCheckBox);
        radioActivityCheckBox = new JCheckBox("Radio Activity", true);
        radioActivityCheckBox.setActionCommand("toggle radio activity");
        radioActivityCheckBox.addActionListener(selectGraphicsHandler);
        graphicsComponentsPanel.add(radioActivityCheckBox);
        arrowCheckBox = new JCheckBox("Scale arrow", true);
        arrowCheckBox.setActionCommand("toggle arrow");
        arrowCheckBox.addActionListener(selectGraphicsHandler);
        graphicsComponentsPanel.add(arrowCheckBox);
        graphicsComponentsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        graphicsComponentsPanel.add(new JLabel("Obstacle configuration:"));
        noneButton = new JRadioButton("No obstacles");
        noneButton.setActionCommand("set no obstacles");
        noneButton.addActionListener(obstacleHandler);
        noneButton.setSelected(true);
        JRadioButton pre1Button = new JRadioButton("Predefined 1");
        pre1Button.setActionCommand("set predefined 1");
        pre1Button.addActionListener(obstacleHandler);
        JRadioButton pre2Button = new JRadioButton("Predefined 2");
        pre2Button.setActionCommand("set predefined 2");
        pre2Button.addActionListener(obstacleHandler);
        JRadioButton customButton = new JRadioButton("From bitmap");
        customButton.setActionCommand("set custom bitmap");
        customButton.addActionListener(obstacleHandler);
        if (GUI.isVisualizedInApplet()) {
            customButton.setEnabled(false);
        }
        group = new ButtonGroup();
        group.add(noneButton);
        group.add(pre1Button);
        group.add(pre2Button);
        group.add(customButton);
        graphicsComponentsPanel.add(noneButton);
        graphicsComponentsPanel.add(pre1Button);
        graphicsComponentsPanel.add(pre2Button);
        graphicsComponentsPanel.add(customButton);
        JPanel visualizeChannelPanel = new JPanel();
        visualizeChannelPanel.setLayout(new BoxLayout(visualizeChannelPanel, BoxLayout.Y_AXIS));
        visualizeChannelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        visualizeChannelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        visualizeChannelPanel.add(new JLabel("Channel coloring:"));
        JPanel fixedVsRelative = new JPanel(new GridLayout(1, 2));
        JRadioButton fixedColoringButton = new JRadioButton("Fixed");
        fixedColoringButton.setSelected(true);
        fixedColoringButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                coloringIsFixed = true;
            }
        });
        fixedVsRelative.add(fixedColoringButton);
        JRadioButton relativeColoringButton = new JRadioButton("Relative");
        relativeColoringButton.setSelected(true);
        relativeColoringButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                coloringIsFixed = false;
            }
        });
        fixedVsRelative.add(relativeColoringButton);
        ButtonGroup coloringGroup = new ButtonGroup();
        coloringGroup.add(fixedColoringButton);
        coloringGroup.add(relativeColoringButton);
        fixedVsRelative.setAlignmentX(Component.LEFT_ALIGNMENT);
        visualizeChannelPanel.add(fixedVsRelative);
        coloringIntervalPanel = new JPanel() {

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                int width = getWidth();
                int height = getHeight();
                double diff = coloringHighest - coloringLowest;
                int textHeight = g.getFontMetrics().getHeight();
                if (attenuatorThread != null && attenuatorThread.isAlive()) {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, width, height);
                    g.setColor(Color.BLACK);
                    String stringToDraw = "[calculating]";
                    g.drawString(stringToDraw, width / 2 - g.getFontMetrics().stringWidth(stringToDraw) / 2, height / 2 + textHeight / 2);
                    return;
                }
                if (Double.isInfinite(coloringHighest) || Double.isInfinite(coloringLowest)) {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, width, height);
                    g.setColor(Color.BLACK);
                    String stringToDraw = "INFINITE VALUES EXIST";
                    g.drawString(stringToDraw, width / 2 - g.getFontMetrics().stringWidth(stringToDraw) / 2, height / 2 + textHeight / 2);
                    return;
                }
                if (diff == 0) {
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, width, height);
                    g.setColor(Color.BLACK);
                    NumberFormat formatter = DecimalFormat.getNumberInstance();
                    String stringToDraw = "CONSTANT VALUES (" + formatter.format(coloringHighest) + ")";
                    g.drawString(stringToDraw, width / 2 - g.getFontMetrics().stringWidth(stringToDraw) / 2, height / 2 + textHeight / 2);
                    return;
                }
                for (int i = 0; i < width; i++) {
                    double paintValue = coloringLowest + (double) i / (double) width * diff;
                    g.setColor(new Color(getColorOfSignalStrength(paintValue, coloringLowest, coloringHighest)));
                    g.drawLine(i, 0, i, height);
                }
                if (dataTypeToVisualize == ChannelModel.TransmissionData.PROB_OF_RECEPTION) {
                    NumberFormat formatter = DecimalFormat.getPercentInstance();
                    g.setColor(Color.BLACK);
                    g.drawString(formatter.format(coloringLowest), 3, textHeight);
                    String stringToDraw = formatter.format(coloringHighest);
                    g.drawString(stringToDraw, width - g.getFontMetrics().stringWidth(stringToDraw) - 3, textHeight);
                } else if (dataTypeToVisualize == ChannelModel.TransmissionData.SIGNAL_STRENGTH || dataTypeToVisualize == ChannelModel.TransmissionData.SIGNAL_STRENGTH_VAR) {
                    NumberFormat formatter = DecimalFormat.getNumberInstance();
                    g.setColor(Color.BLACK);
                    g.drawString(formatter.format(coloringLowest) + "dBm", 3, textHeight);
                    String stringToDraw = formatter.format(coloringHighest) + "dBm";
                    g.drawString(stringToDraw, width - g.getFontMetrics().stringWidth(stringToDraw) - 3, textHeight);
                } else if (dataTypeToVisualize == ChannelModel.TransmissionData.SNR || dataTypeToVisualize == ChannelModel.TransmissionData.SNR_VAR) {
                    NumberFormat formatter = DecimalFormat.getNumberInstance();
                    g.setColor(Color.BLACK);
                    g.drawString(formatter.format(coloringLowest) + "dB", 3, textHeight);
                    String stringToDraw = formatter.format(coloringHighest) + "dB";
                    g.drawString(stringToDraw, width - g.getFontMetrics().stringWidth(stringToDraw) - 3, textHeight);
                } else if (dataTypeToVisualize == ChannelModel.TransmissionData.DELAY_SPREAD_RMS) {
                    NumberFormat formatter = DecimalFormat.getNumberInstance();
                    g.setColor(Color.BLACK);
                    g.drawString(formatter.format(coloringLowest) + "us", 3, textHeight);
                    String stringToDraw = formatter.format(coloringHighest) + "us";
                    g.drawString(stringToDraw, width - g.getFontMetrics().stringWidth(stringToDraw) - 3, textHeight);
                }
            }
        };
        coloringIntervalPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        Dimension colorPanelSize = new Dimension(200, 20);
        coloringIntervalPanel.setPreferredSize(colorPanelSize);
        coloringIntervalPanel.setMinimumSize(colorPanelSize);
        coloringIntervalPanel.setMaximumSize(colorPanelSize);
        coloringIntervalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        visualizeChannelPanel.add(coloringIntervalPanel);
        visualizeChannelPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        visualizeChannelPanel.add(new JLabel("Visualize:"));
        JRadioButton signalStrengthButton = new JRadioButton("Signal strength");
        signalStrengthButton.setActionCommand("signalStrengthButton");
        signalStrengthButton.setSelected(true);
        signalStrengthButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dataTypeToVisualize = ChannelModel.TransmissionData.SIGNAL_STRENGTH;
            }
        });
        visualizeChannelPanel.add(signalStrengthButton);
        JRadioButton signalStrengthVarButton = new JRadioButton("Signal strength variance");
        signalStrengthVarButton.setActionCommand("signalStrengthVarButton");
        signalStrengthVarButton.setSelected(false);
        signalStrengthVarButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dataTypeToVisualize = ChannelModel.TransmissionData.SIGNAL_STRENGTH_VAR;
            }
        });
        visualizeChannelPanel.add(signalStrengthVarButton);
        JRadioButton SNRButton = new JRadioButton("Signal to Noise ratio");
        SNRButton.setActionCommand("SNRButton");
        SNRButton.setSelected(false);
        SNRButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dataTypeToVisualize = ChannelModel.TransmissionData.SNR;
            }
        });
        visualizeChannelPanel.add(SNRButton);
        JRadioButton SNRVarButton = new JRadioButton("Signal to Noise variance");
        SNRVarButton.setActionCommand("SNRVarButton");
        SNRVarButton.setSelected(false);
        SNRVarButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dataTypeToVisualize = ChannelModel.TransmissionData.SNR_VAR;
            }
        });
        visualizeChannelPanel.add(SNRVarButton);
        JRadioButton probabilityButton = new JRadioButton("Probability of reception");
        probabilityButton.setActionCommand("probabilityButton");
        probabilityButton.setSelected(false);
        probabilityButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dataTypeToVisualize = ChannelModel.TransmissionData.PROB_OF_RECEPTION;
            }
        });
        visualizeChannelPanel.add(probabilityButton);
        JRadioButton rmsDelaySpreadButton = new JRadioButton("RMS delay spread");
        rmsDelaySpreadButton.setActionCommand("rmsDelaySpreadButton");
        rmsDelaySpreadButton.setSelected(false);
        rmsDelaySpreadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dataTypeToVisualize = ChannelModel.TransmissionData.DELAY_SPREAD_RMS;
            }
        });
        visualizeChannelPanel.add(rmsDelaySpreadButton);
        visTypeSelectionGroup = new ButtonGroup();
        visTypeSelectionGroup.add(signalStrengthButton);
        visTypeSelectionGroup.add(signalStrengthVarButton);
        visTypeSelectionGroup.add(SNRButton);
        visTypeSelectionGroup.add(SNRVarButton);
        visTypeSelectionGroup.add(probabilityButton);
        visTypeSelectionGroup.add(rmsDelaySpreadButton);
        visualizeChannelPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        visualizeChannelPanel.add(new JLabel("Resolution:"));
        resolutionSlider = new JSlider(JSlider.HORIZONTAL, 30, 600, 100);
        resolutionSlider.setMajorTickSpacing(100);
        resolutionSlider.setPaintTicks(true);
        resolutionSlider.setPaintLabels(true);
        resolutionSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        visualizeChannelPanel.add(resolutionSlider);
        visualizeChannelPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        JButton recalculateVisibleButton = new JButton("Paint radio channel");
        recalculateVisibleButton.setActionCommand("recalculate visible area");
        recalculateVisibleButton.addActionListener(formulaHandler);
        visualizeChannelPanel.add(recalculateVisibleButton);
        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        graphicsComponentsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(graphicsComponentsPanel);
        controlPanel.add(new JSeparator());
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        visualizeChannelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(visualizeChannelPanel);
        controlPanel.setPreferredSize(new Dimension(250, 700));
        controlPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollControlPanel = new JScrollPane(controlPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollControlPanel.setPreferredSize(new Dimension(250, 0));
        this.setLayout(new BorderLayout());
        this.add(BorderLayout.CENTER, canvas);
        this.add(BorderLayout.EAST, scrollControlPanel);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        URL imageURL = this.getClass().getClassLoader().getResource(antennaImageFilename);
        antennaImage = toolkit.getImage(imageURL);
        MediaTracker tracker = new MediaTracker(canvas);
        tracker.addImage(antennaImage, 1);
        try {
            tracker.waitForAll();
        } catch (InterruptedException ex) {
            logger.fatal("Interrupted during image loading, aborting");
            return;
        }
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }
