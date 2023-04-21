    private void createPanelWIDTH_VS_AMP() {
        childGraphPanels[1] = new JPanel();
        childGraphPanels[1].setLayout(new BorderLayout());
        childControlPanels[1] = new JPanel();
        childControlPanels[1].setLayout(new BorderLayout());
        widthVsAmpGraph.setOffScreenImageDrawing(true);
        widthVsAmpGraph.setName("WIDTH vs. NORMALIZED AMPLITUDE");
        widthVsAmpGraph.setAxisNames("Ampl./Design Ampl", "Phase Width [grd]");
        widthVsAmpGraph.setGraphBackGroundColor(Color.white);
        widthVsAmpGraph.setLegendButtonVisible(true);
        widthVsAmpGraph.setLegendBackground(Color.white);
        widthVsAmpGraph.setLegendVisible(true);
        maxValVsAmpGraph.setOffScreenImageDrawing(true);
        maxValVsAmpGraph.setName("MAX. TRANSMISSION vs. NORMALIZED AMPLITUDE");
        maxValVsAmpGraph.setAxisNames("Ampl./Design Ampl", "Transmission");
        maxValVsAmpGraph.setGraphBackGroundColor(Color.white);
        maxValVsAmpGraph.setLegendButtonVisible(true);
        maxValVsAmpGraph.setLegendBackground(Color.white);
        maxValVsAmpGraph.setLegendVisible(true);
        widthVsAmpGraph.addGraphData(gdP1_wFa);
        widthVsAmpGraph.addGraphData(gdP1_Exp_wFa);
        maxValVsAmpGraph.addGraphData(gdP1_maxVsA);
        gdP1_wFa.setGraphProperty(graphAnalysis.getLegendKeyString(), "THEORY");
        gdP1_Exp_wFa.setGraphProperty(graphAnalysis.getLegendKeyString(), "MEASUREMENTS");
        gdP1_maxVsA.setGraphProperty(graphAnalysis.getLegendKeyString(), "MEASUREMENTS");
        gdP1_wFa.setImmediateContainerUpdate(false);
        gdP1_Exp_wFa.setImmediateContainerUpdate(false);
        gdP1_maxVsA.setImmediateContainerUpdate(false);
        gdP1_wFa.setDrawLinesOn(true);
        gdP1_wFa.setDrawPointsOn(false);
        gdP1_wFa.setLineThick(3);
        gdP1_wFa.setGraphColor(Color.blue);
        enrgDltP1_Text.setEditable(false);
        enrgDltP1_Text.setDecimalFormat(ampFormat);
        enrgDltP1_Text.setHorizontalAlignment(JTextField.CENTER);
        enrgDltP1_Text.removeInnerFocusListener();
        enrgDltP1_Text.setText(null);
        enrgDltP1_Text.setBackground(Color.white);
        guessAmpP1_Text.setEditable(false);
        guessAmpP1_Text.setDecimalFormat(ampFormat);
        guessAmpP1_Text.setHorizontalAlignment(JTextField.CENTER);
        guessAmpP1_Text.removeInnerFocusListener();
        guessAmpP1_Text.setText(null);
        guessAmpP1_Text.setBackground(Color.white);
        guessPhaseP1_Text.setEditable(false);
        guessPhaseP1_Text.setDecimalFormat(phaseFormat);
        guessPhaseP1_Text.setHorizontalAlignment(JTextField.CENTER);
        guessPhaseP1_Text.removeInnerFocusListener();
        guessPhaseP1_Text.setText(null);
        guessPhaseP1_Text.setBackground(Color.white);
        setEnrgDltP1_Button.setForeground(Color.blue);
        setGuessAmpP1_Button.setForeground(Color.blue);
        setEnrgDltP1_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                double val = enrgDltP1_Text.getValue();
                if (enrgDltP1_Text.getText().length() > 0) {
                    designEnrgDevText.setValue(val);
                }
            }
        });
        setGuessAmpP1_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                double ampVal = guessAmpP1_Text.getValue();
                double phaseVal = guessPhaseP0_Text.getValue();
                if (scanVariableParameter.getChannel() != null && scanVariable.getChannel() != null) {
                    scanVariableParameter.setValue(ampVal);
                    scanVariable.setValue(phaseVal);
                } else {
                    messageTextLocal.setText(null);
                    messageTextLocal.setText("The parameter PV channel does not exist.");
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        JPanel tmp_0 = new JPanel();
        tmp_0.setLayout(new GridLayout(3, 2, 1, 1));
        Border etchedBorder = BorderFactory.createEtchedBorder();
        tmp_0.setBorder(etchedBorder);
        tmp_0.add(enrgDltP1_Label);
        tmp_0.add(enrgDltP1_Text);
        tmp_0.add(guessAmpP1_Label);
        tmp_0.add(guessAmpP1_Text);
        tmp_0.add(guessPhaseP1_Label);
        tmp_0.add(guessPhaseP1_Text);
        JPanel tmp_1 = new JPanel();
        tmp_1.setLayout(new BorderLayout());
        tmp_1.setBorder(etchedBorder);
        tmp_1.setBackground(tmp_0.getBackground().darker());
        tmp_1.add(setEnrgDltP1_Button, BorderLayout.NORTH);
        tmp_1.add(tmp_0, BorderLayout.CENTER);
        tmp_1.add(setGuessAmpP1_Button, BorderLayout.SOUTH);
        childControlPanels[1].add(tmp_1, BorderLayout.NORTH);
        JPanel tmp_10 = new JPanel();
        tmp_10.setLayout(new GridLayout(2, 1, 1, 1));
        tmp_10.add(widthVsAmpGraph);
        tmp_10.add(maxValVsAmpGraph);
        childGraphPanels[1].add(tmp_10, BorderLayout.CENTER);
    }
