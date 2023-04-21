    public ScanController1D(String title) {
        this.title = title;
        lowLimText.setNormalBackground(Color.white);
        uppLimText.setNormalBackground(Color.white);
        stepText.setNormalBackground(Color.white);
        sleepTimeText.setNormalBackground(Color.white);
        valueText.setBackground(Color.getHSBColor(0.5f, 0.5f, 1.0f));
        valueTextRB.setBackground(Color.getHSBColor(0.0f, 0.0f, 0.9f));
        valueTextRB.setEditable(false);
        lowLimText.setDecimalFormat(valueFormat);
        uppLimText.setDecimalFormat(valueFormat);
        stepText.setDecimalFormat(valueFormat);
        sleepTimeText.setDecimalFormat(sleepTimeFormat);
        lowLimText.setHorizontalAlignment(JTextField.CENTER);
        uppLimText.setHorizontalAlignment(JTextField.CENTER);
        valueText.setHorizontalAlignment(JTextField.CENTER);
        valueTextRB.setHorizontalAlignment(JTextField.CENTER);
        stepText.setHorizontalAlignment(JTextField.CENTER);
        sleepTimeText.setHorizontalAlignment(JTextField.CENTER);
        valueText.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (scanOn == false) {
                    try {
                        scanValue = Double.parseDouble(valueText.getText());
                    } catch (NumberFormatException exc) {
                    }
                    setCurrentValue(scanValue);
                    boolean containersCreated = false;
                    for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
                        MeasuredValue mv_tmp = measuredValuesV.get(i);
                        if (mv_tmp != null && mv_tmp.getNumberOfDataContainers() == 0) {
                            mv_tmp.createNewDataContainer();
                            containersCreated = true;
                            if (scanVariable.getChannelRB() != null && mv_tmp.getNumberOfDataContainersRB() == 0) {
                                mv_tmp.createNewDataContainerRB();
                            }
                        }
                    }
                    if (containersCreated) {
                        for (int i = 0, n = newSetOfDataListenersV.size(); i < n; i++) {
                            newSetOfDataListenersV.get(i).actionPerformed(newSetOfDataAction);
                        }
                    }
                    measure(scanValue);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    setCurrentValue(scanValue);
                }
            }
        });
        valueText.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (scanOn == false) {
                        try {
                            scanValue = Double.parseDouble(valueText.getText());
                        } catch (NumberFormatException exc) {
                        }
                        setCurrentValue(scanValue);
                        boolean containersCreated = false;
                        for (int i = 0, n = measuredValuesV.size(); i < n; i++) {
                            MeasuredValue mv_tmp = measuredValuesV.get(i);
                            if (mv_tmp != null && mv_tmp.getNumberOfDataContainers() == 0) {
                                mv_tmp.createNewDataContainer();
                                containersCreated = true;
                                if (scanVariable.getChannelRB() != null && mv_tmp.getNumberOfDataContainersRB() == 0) {
                                    mv_tmp.createNewDataContainerRB();
                                }
                            }
                        }
                        if (containersCreated) {
                            for (int i = 0, n = newSetOfDataListenersV.size(); i < n; i++) {
                                newSetOfDataListenersV.get(i).actionPerformed(newSetOfDataAction);
                            }
                        }
                        measure(scanValue);
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                        setCurrentValue(scanValue);
                    }
                }
            }
        });
        valueTextRB.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                valueTextRB.setText(null);
                if (scanVariable != null && scanOn == false) {
                    scanValueRB = scanVariable.getValueRB();
                    if (scanVariable.getChannelRB() != null) {
                        scanValueRB = scanVariable.getValueRB();
                        valueTextRB.setText(valueFormat.format(scanValueRB));
                    }
                }
            }
        });
        lowLimText.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                lowLim = lowLimText.getValue();
                setSliderValue(scanValue);
                continueMode = false;
                setButtonsState(START_BUTTONS_STATE);
            }
        });
        uppLimText.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                uppLim = uppLimText.getValue();
                setSliderValue(scanValue);
                continueMode = false;
                setButtonsState(START_BUTTONS_STATE);
            }
        });
        stepText.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                step = stepText.getValue();
                continueMode = false;
                setButtonsState(START_BUTTONS_STATE);
            }
        });
        sleepTimeText.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                sleepTime = sleepTimeText.getValue();
            }
        });
        scrollBar.setBlockIncrement((scrollBar.getMaximum() - scrollBar.getMinimum()) / 50);
        scrollBar.getModel().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (!scrollBarLocked) {
                    int i_val = scrollBar.getValue();
                    double val = lowLim + i_val * (uppLim - lowLim) / (scrollBar.getMaximum() - scrollBar.getMinimum());
                    valueText.setText(null);
                    valueText.setText(valueFormat.format(val));
                }
            }
        });
        setButtonsState(START_BUTTONS_STATE);
        startButton.setHorizontalTextPosition(SwingConstants.CENTER);
        stopButton.setHorizontalTextPosition(SwingConstants.CENTER);
        resumeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        startButtonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (scanOn == true) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                messageText.setText(null);
                continueMode = false;
                scanVarShouldBeRestored = true;
                if (CURRENT_BUTTONS_STATE == START_BUTTONS_STATE) {
                    scanVarShouldBeMemorized = true;
                } else {
                    scanVarShouldBeMemorized = false;
                }
                setButtonsState(SCAN_BUTTONS_STATE);
                for (int i = 0, n = startListenersV.size(); i < n; i++) {
                    startListenersV.get(i).actionPerformed(startButtonAction);
                }
                measure();
            }
        };
        startButton.addActionListener(startButtonListener);
        resumeButtonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                scanVarShouldBeMemorized = false;
                if (CURRENT_BUTTONS_STATE == RESUME_BUTTONS_STATE) {
                    if (scanOn == true) {
                        Toolkit.getDefaultToolkit().beep();
                        return;
                    }
                    messageText.setText(null);
                    continueMode = true;
                    scanVarShouldBeRestored = true;
                    setButtonsState(SCAN_BUTTONS_STATE);
                    for (int i = 0, n = resumeListenersV.size(); i < n; i++) {
                        resumeListenersV.get(i).actionPerformed(resumeButtonAction);
                    }
                    measure();
                } else {
                    if (scanOn == false) {
                        Toolkit.getDefaultToolkit().beep();
                        return;
                    }
                    scanOn = false;
                    scanVarShouldBeRestored = false;
                    if (measurementThread != null && measurementThread.isAlive()) {
                        measurementThread.interrupt();
                    }
                }
            }
        };
        resumeButton.addActionListener(resumeButtonListener);
        stopButtonListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                scanVarShouldBeMemorized = false;
                if (scanOn == false) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                scanOn = false;
                scanVarShouldBeRestored = true;
                if (measurementThread != null && measurementThread.isAlive()) {
                    measurementThread.interrupt();
                }
            }
        };
        stopButton.addActionListener(stopButtonListener);
        stopScanListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                scanOn = false;
                if (measurementThread != null && measurementThread.isAlive()) {
                    measurementThread.interrupt();
                }
            }
        };
        newSetOfDataAction = new ActionEvent(this, 0, "newSet");
        newPointOfDataAction = new ActionEvent(this, 0, "newPoint");
        startButtonAction = new ActionEvent(this, 0, "startButton");
        stopButtonAction = new ActionEvent(this, 0, "stopButton");
        resumeButtonAction = new ActionEvent(this, 0, "resumeButton");
        setCurrentValueRB(scanValue);
        setCurrentValue(scanValueRB);
        setLowLimit(lowLim);
        setUppLimit(uppLim);
        setStep(step);
        setSleepTime(sleepTime);
        phaseScan_Button.setToolTipText("Use this button to indicate a phase scan");
        controllerPanel.setLayout(new BorderLayout());
        controllerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title));
        FlowLayout flwC = new FlowLayout(FlowLayout.LEFT, 1, 1);
        JPanel panel_1 = phaseScanAndRB_Panel;
        panel_1.setBorder(BorderFactory.createEmptyBorder());
        panel_1.setLayout(flwC);
        panel_1.add(valueRB_Label);
        panel_1.add(valueTextRB);
        JPanel panel_2 = new JPanel();
        panel_2.setBorder(BorderFactory.createEmptyBorder());
        panel_2.setLayout(new GridLayout(1, 3, 1, 1));
        panel_2.add(lowLimText);
        panel_2.add(valueText);
        panel_2.add(uppLimText);
        JPanel panel_3 = new JPanel();
        panel_3.setBorder(BorderFactory.createEmptyBorder());
        panel_3.setLayout(new BorderLayout());
        panel_3.add(scrollBar, BorderLayout.NORTH);
        JPanel panel_4 = new JPanel();
        panel_4.setBorder(BorderFactory.createEmptyBorder());
        panel_4.setLayout(flwC);
        panel_4.add(scanStep_Label);
        panel_4.add(stepText);
        panel_4.add(unitsLabel);
        JPanel panel_5 = new JPanel();
        panel_5.setBorder(BorderFactory.createEmptyBorder());
        panel_5.setLayout(flwC);
        panel_5.add(sleepTimeLabel);
        panel_5.add(sleepTimeText);
        JPanel panel_6 = new JPanel();
        panel_6.setBorder(BorderFactory.createEmptyBorder());
        panel_6.setLayout(new GridLayout(1, 3, 1, 1));
        panel_6.add(startButton);
        panel_6.add(resumeButton);
        panel_6.add(stopButton);
        JPanel panel_7 = new JPanel();
        panel_7.setBorder(BorderFactory.createEmptyBorder());
        panel_7.setLayout(flwC);
        panel_7.add(beamTrigger.getJPanel());
        JPanel inner_panel = new JPanel();
        inner_panel.setLayout(new VerticalLayout());
        inner_panel.add(panel_1);
        inner_panel.add(panel_2);
        inner_panel.add(panel_3);
        inner_panel.add(panel_4);
        inner_panel.add(panel_5);
        inner_panel.add(panel_6);
        inner_panel.add(panel_7);
        controllerPanel.add(inner_panel, BorderLayout.WEST);
        setFontForAll(new Font("Monospaced", Font.PLAIN, 10));
        controllerPanel.setBackground(controllerPanel.getBackground().darker());
    }
