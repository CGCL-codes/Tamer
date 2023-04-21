    private void createPanelFIND_WIDTH() {
        childGraphPanels[0] = new JPanel();
        childGraphPanels[0].setLayout(new BorderLayout());
        childControlPanels[0] = new JPanel();
        childControlPanels[0].setLayout(new BorderLayout());
        paramPV_ValueText.setEditable(false);
        paramPV_RB_ValueText.setEditable(false);
        paramPV_ValueText.setDecimalFormat(ampFormat);
        paramPV_RB_ValueText.setDecimalFormat(ampFormat);
        paramPV_ValueText.setHorizontalAlignment(JTextField.CENTER);
        paramPV_RB_ValueText.setHorizontalAlignment(JTextField.CENTER);
        paramPV_ValueText.removeInnerFocusListener();
        paramPV_RB_ValueText.removeInnerFocusListener();
        paramPV_ValueText.setText(null);
        paramPV_ValueText.setBackground(Color.white);
        paramPV_RB_ValueText.setText(null);
        paramPV_RB_ValueText.setBackground(Color.white);
        widthP0_Text.setEditable(false);
        guessAmpP0_Text.setEditable(false);
        guessPhaseP0_Text.setEditable(false);
        widthP0_Text.setDecimalFormat(ampFormat);
        guessAmpP0_Text.setDecimalFormat(ampFormat);
        guessPhaseP0_Text.setDecimalFormat(phaseFormat);
        widthP0_Text.setHorizontalAlignment(JTextField.CENTER);
        guessAmpP0_Text.setHorizontalAlignment(JTextField.CENTER);
        guessPhaseP0_Text.setHorizontalAlignment(JTextField.CENTER);
        widthP0_Text.removeInnerFocusListener();
        guessAmpP0_Text.removeInnerFocusListener();
        guessPhaseP0_Text.removeInnerFocusListener();
        widthP0_Text.setText(null);
        widthP0_Text.setBackground(Color.white);
        guessAmpP0_Text.setText(null);
        guessAmpP0_Text.setBackground(Color.white);
        guessPhaseP0_Text.setText(null);
        guessPhaseP0_Text.setBackground(Color.white);
        findWidthP0_Button.setForeground(Color.blue);
        setGuessAmpP0_Button.setForeground(Color.blue);
        graphChooserListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Integer Ind = graphAnalysis.getGraphChosenIndex();
                if (Ind != null && Ind.intValue() >= 0) {
                    int ind = Ind.intValue();
                    BasicGraphData gd = graphAnalysis.getInstanceOfGraphData(ind);
                    Double parD = (Double) gd.getGraphProperty("PARAMETER_VALUE");
                    if (parD != null) {
                        paramPV_ValueText.setValue(parD.doubleValue());
                    } else {
                        paramPV_ValueText.setText(null);
                        paramPV_ValueText.setBackground(Color.white);
                    }
                    parD = (Double) gd.getGraphProperty("PARAMETER_VALUE_RB");
                    if (parD != null) {
                        paramPV_RB_ValueText.setValue(parD.doubleValue());
                    } else {
                        paramPV_RB_ValueText.setText(null);
                        paramPV_RB_ValueText.setBackground(Color.white);
                    }
                } else {
                    paramPV_ValueText.setText(null);
                    paramPV_ValueText.setBackground(Color.white);
                    paramPV_RB_ValueText.setText(null);
                    paramPV_RB_ValueText.setBackground(Color.white);
                }
                widthP0_Text.setText(null);
                widthP0_Text.setBackground(Color.white);
                guessAmpP0_Text.setText(null);
                guessAmpP0_Text.setBackground(Color.white);
                guessPhaseP0_Text.setText(null);
                guessPhaseP0_Text.setBackground(Color.white);
            }
        };
        graphChooserMouseAdapter = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                Integer Ind = graphAnalysis.getGraphChosenIndex();
                if (Ind == null || Ind.intValue() < 0) {
                    paramPV_ValueText.setText(null);
                    paramPV_ValueText.setBackground(Color.white);
                    paramPV_RB_ValueText.setText(null);
                    paramPV_RB_ValueText.setBackground(Color.white);
                }
            }
        };
        findWidthP0_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                BasicGraphData gd = mainController.getChoosenDraphData();
                if (gd != null) {
                    Double[] resArr = findWidthAndPlot(gd);
                    Double widthD = resArr[0];
                    Double phaseLD = resArr[1];
                    Double phaseRD = resArr[2];
                    if (widthD != null && phaseLD != null && phaseRD != null) {
                        double energyDlt = designEnrgDevText.getValue();
                        makeForwardAndBackWardGraphs(energyDlt);
                        double newAmpNorm = gdP1_aFw.getValueY(widthD.doubleValue());
                        double amp = ((Double) gd.getGraphProperty("PARAMETER_VALUE")).doubleValue();
                        amp = amp / newAmpNorm;
                        double k_shift = gdP1_ksFa.getValueY(newAmpNorm);
                        double phase_guess = phaseLD.doubleValue() + k_shift * (phaseRD.doubleValue() - phaseLD.doubleValue());
                        double phase_shift = mainController.getPhaseShift(gd);
                        double markerPos = phase_guess - phase_shift;
                        if (phase_shift != 0.) {
                            markerPos += 180.;
                            while (markerPos < 0.) {
                                markerPos += 360.;
                            }
                            markerPos = markerPos % 360.;
                            markerPos -= 180.;
                        }
                        System.out.println("debug new point  newAmpNorm=" + ampFormat.format(newAmpNorm) + " Edlt=" + ampFormat.format(energyDlt) + " w=" + ampFormat.format(widthD.doubleValue()) + " amp/guessA=" + ampFormat.format(amp) + " phiL=" + ampFormat.format(phaseLD.doubleValue()) + " phiR=" + ampFormat.format(phaseRD.doubleValue()) + " phi=" + ampFormat.format(phase_guess) + " k_s=" + ampFormat.format(k_shift) + " phase_shift=" + ampFormat.format(phase_shift));
                        guessAmpP0_Text.setValue(amp);
                        guessPhaseP0_Text.setValue(markerPos);
                    }
                } else {
                    widthP0_Text.setText(null);
                    widthP0_Text.setBackground(Color.white);
                    guessAmpP0_Text.setText(null);
                    guessAmpP0_Text.setBackground(Color.white);
                    Toolkit.getDefaultToolkit().beep();
                    messageTextLocal.setText(null);
                    messageTextLocal.setText("Please choose the graph first. Use S-button on the graph panel.");
                }
            }
        });
        setGuessAmpP0_Button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                double ampVal = guessAmpP0_Text.getValue();
                double phaseVal = guessPhaseP0_Text.getValue();
                if (scanVariableParameter.getChannel() != null && scanVariable.getChannel() != null) {
                    scanVariableParameter.setValue(ampVal);
                    scanVariable.setValue(phaseVal);
                } else {
                    messageTextLocal.setText(null);
                    messageTextLocal.setText("The parameter or Scan Variable PV channel does not exist.");
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        JPanel tmp_0 = new JPanel();
        tmp_0.setLayout(new GridLayout(2, 2, 1, 1));
        Border etchedBorder = BorderFactory.createEtchedBorder();
        tmp_0.setBorder(etchedBorder);
        tmp_0.setBackground(tmp_0.getBackground().darker());
        tmp_0.add(paramPV_Label);
        tmp_0.add(paramPV_ValueText);
        tmp_0.add(paramPV_RB_Label);
        tmp_0.add(paramPV_RB_ValueText);
        JPanel tmp_1 = new JPanel();
        tmp_1.setLayout(new BorderLayout());
        tmp_1.setBorder(etchedBorder);
        tmp_1.setBackground(tmp_0.getBackground().darker());
        JPanel tmp_2 = new JPanel();
        tmp_2.setLayout(new GridLayout(3, 2, 1, 1));
        tmp_2.add(widthP0_Label);
        tmp_2.add(widthP0_Text);
        tmp_2.add(guessAmpP0_Label);
        tmp_2.add(guessAmpP0_Text);
        tmp_2.add(guessPhaseP0_Label);
        tmp_2.add(guessPhaseP0_Text);
        tmp_1.add(findWidthP0_Button, BorderLayout.NORTH);
        tmp_1.add(tmp_2, BorderLayout.CENTER);
        tmp_1.add(setGuessAmpP0_Button, BorderLayout.SOUTH);
        childControlPanels[0].add(tmp_0, BorderLayout.NORTH);
        childControlPanels[0].add(tmp_1, BorderLayout.CENTER);
    }
