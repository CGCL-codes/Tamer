    public Scan2D(String title) {
        this.title = title;
        paramLowLimText.setNormalBackground(Color.white);
        paramUppLimText.setNormalBackground(Color.white);
        paramStepText.setNormalBackground(Color.white);
        paramText.setBackground(Color.getHSBColor(0.5f, 0.5f, 1.0f));
        paramTextRB.setBackground(Color.getHSBColor(0.0f, 0.0f, 0.9f));
        paramTextRB.setEditable(false);
        paramLowLimText.setDecimalFormat(valueFormat);
        paramUppLimText.setDecimalFormat(valueFormat);
        paramStepText.setDecimalFormat(valueFormat);
        paramLowLimText.setHorizontalAlignment(JTextField.CENTER);
        paramUppLimText.setHorizontalAlignment(JTextField.CENTER);
        paramText.setHorizontalAlignment(JTextField.CENTER);
        paramStepText.setHorizontalAlignment(JTextField.CENTER);
        paramTextRB.setHorizontalAlignment(JTextField.CENTER);
        paramText.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (scanOn == false) {
                    try {
                        paramValue = Double.parseDouble(paramText.getText());
                    } catch (NumberFormatException exc) {
                    }
                    setParamCurrentValue(paramValue);
                    measure(paramValue);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        paramText.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (scanOn == false) {
                        try {
                            paramValue = Double.parseDouble(paramText.getText());
                        } catch (NumberFormatException exc) {
                        }
                        setParamCurrentValue(paramValue);
                        measure(paramValue);
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
            }
        });
        paramTextRB.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                paramTextRB.setText(null);
                if (paramVariable != null && paramVariable.getChannelRB() != null) {
                    paramValueRB = paramVariable.getValueRB();
                    paramTextRB.setText(valueFormat.format(paramValueRB));
                }
            }
        });
        paramLowLimText.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                paramLowLim = paramLowLimText.getValue();
                setParamSliderValue(paramValue);
                continueMode = false;
                setButtonsState(START_BUTTONS_STATE);
            }
        });
        paramUppLimText.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                paramUppLim = paramUppLimText.getValue();
                setParamSliderValue(paramValue);
                continueMode = false;
                setButtonsState(START_BUTTONS_STATE);
            }
        });
        paramStepText.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                paramStep = paramStepText.getValue();
                continueMode = false;
                setButtonsState(START_BUTTONS_STATE);
            }
        });
        paramScrollBar.setBlockIncrement((scrollBar.getMaximum() - scrollBar.getMinimum()) / 50);
        paramScrollBar.getModel().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (!paramScrollBarLocked) {
                    int i_val = paramScrollBar.getValue();
                    double val = paramLowLim + i_val * (paramUppLim - paramLowLim) / (paramScrollBar.getMaximum() - paramScrollBar.getMinimum());
                    paramText.setText(null);
                    paramText.setText(valueFormat.format(val));
                }
            }
        });
        lowLimText.setNormalBackground(Color.white);
        uppLimText.setNormalBackground(Color.white);
        stepText.setNormalBackground(Color.white);
        sleepTimeText.setNormalBackground(Color.white);
        valueText.setBackground(Color.getHSBColor(0.0f, 0.0f, 0.9f));
        valueTextRB.setBackground(Color.getHSBColor(0.0f, 0.0f, 0.9f));
        valueTextRB.setEditable(false);
        valueText.setEditable(false);
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
        valueText.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                valueTextRB.setText(null);
                if (scanVariable != null && scanVariable.getMonitoredPV().isGood()) {
                    if (scanVariable.getChannel() != null) {
                        setCurrentValue(scanVariable.getValue());
                    }
                    if (scanVariable.getChannelRB() != null) {
                        setCurrentValueRB(scanVariable.getValueRB());
                    }
                } else {
                    try {
                        scanValue = Double.parseDouble(valueText.getText());
                    } catch (NumberFormatException exc) {
                    }
                    ;
                    setCurrentValue(scanValue);
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
        startButton.addActionListener(new ActionListener() {

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
                for (int i = 0, n = startButtonListenersV.size(); i < n; i++) {
                    ((ActionListener) startButtonListenersV.get(i)).actionPerformed(startButtonAction);
                }
                measure();
            }
        });
        resumeButton.addActionListener(new ActionListener() {

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
        });
        stopButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
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
        });
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
        setParamCurrentValueRB(paramValue);
        setParamCurrentValue(paramValueRB);
        setParamLowLimit(paramLowLim);
        setParamUppLimit(paramUppLim);
        setParamStep(paramStep);
        setCurrentValueRB(scanValue);
        setCurrentValue(scanValueRB);
        setLowLimit(lowLim);
        setUppLimit(uppLim);
        setStep(step);
        setSleepTime(sleepTime);
        controllerPanel.setLayout(new BorderLayout());
        controllerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title));
        FlowLayout flwC = new FlowLayout(FlowLayout.LEFT, 1, 1);
        JPanel panel_1_0 = paramPhaseScanAndRB_Panel;
        panel_1_0.setBorder(BorderFactory.createEmptyBorder());
        panel_1_0.setLayout(flwC);
        panel_1_0.add(paramRB_Label);
        panel_1_0.add(paramTextRB);
        JPanel panel_2_0 = new JPanel();
        panel_2_0.setBorder(BorderFactory.createEmptyBorder());
        panel_2_0.setLayout(new GridLayout(1, 3, 1, 1));
        panel_2_0.add(paramLowLimText);
        panel_2_0.add(paramText);
        panel_2_0.add(paramUppLimText);
        JPanel panel_3_0 = new JPanel();
        panel_3_0.setBorder(BorderFactory.createEmptyBorder());
        panel_3_0.setLayout(new BorderLayout());
        panel_3_0.add(paramScrollBar, BorderLayout.NORTH);
        JPanel panel_4_0 = new JPanel();
        panel_4_0.setBorder(BorderFactory.createEmptyBorder());
        panel_4_0.setLayout(flwC);
        panel_4_0.add(paramScanStep_Label);
        panel_4_0.add(paramStepText);
        panel_4_0.add(paramUnitsLabel);
        JPanel panel_Group_0_0 = new JPanel();
        panel_Group_0_0.setLayout(new BorderLayout());
        panel_Group_0_0.add(panel_2_0, BorderLayout.NORTH);
        panel_Group_0_0.add(panel_3_0, BorderLayout.SOUTH);
        JPanel panel_Group_0_1 = new JPanel();
        panel_Group_0_1.setBorder(BorderFactory.createEtchedBorder());
        panel_Group_0_1.setLayout(new BorderLayout());
        panel_Group_0_1.add(panel_1_0, BorderLayout.NORTH);
        panel_Group_0_1.add(panel_4_0, BorderLayout.SOUTH);
        panel_Group_0_1.add(panel_Group_0_0, BorderLayout.CENTER);
        panel_Group_0_1.setBackground(Color.blue);
        JPanel panel_1 = valuePhaseScanAndRB_Panel;
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
        JPanel panel_Group_1_0 = new JPanel();
        panel_Group_1_0.setLayout(new BorderLayout());
        panel_Group_1_0.add(panel_2, BorderLayout.NORTH);
        panel_Group_1_0.add(panel_3, BorderLayout.SOUTH);
        JPanel panel_Group_1_1 = new JPanel();
        panel_Group_1_1.setBorder(BorderFactory.createEtchedBorder());
        panel_Group_1_1.setLayout(new BorderLayout());
        panel_Group_1_1.add(panel_1, BorderLayout.NORTH);
        panel_Group_1_1.add(panel_4, BorderLayout.SOUTH);
        panel_Group_1_1.add(panel_Group_1_0, BorderLayout.CENTER);
        panel_Group_1_1.setBackground(Color.blue);
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
        JPanel inner_panel = new JPanel();
        inner_panel.setLayout(new VerticalLayout());
        inner_panel.add(panel_Group_0_1);
        inner_panel.add(panel_Group_1_1);
        inner_panel.add(panel_5);
        inner_panel.add(panel_6);
        controllerPanel.add(inner_panel, BorderLayout.WEST);
        setFontForAll(new Font("Monospaced", Font.PLAIN, 10));
        controllerPanel.setBackground(controllerPanel.getBackground().darker());
    }
