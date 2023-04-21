    private void makeScanYPanel() {
        scanYPanel = new JPanel(new BorderLayout());
        scanYPanel.setPreferredSize(new Dimension(200, 400));
        JPanel tmp_0 = new JPanel();
        tmp_0.setLayout(new VerticalLayout());
        JPanel setStrobePanel = new JPanel();
        setStrobePanel.setLayout(new VerticalLayout());
        setStrobePanel.add(setStrobeRadioY);
        tmp_0.add(setStrobePanel);
        setStrobeRadioY.setEnabled(false);
        setStrobeRadioY.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (setStrobeRadioY.isSelected()) {
                    System.out.println("Strobe PV Name: " + theDoc.selectedQuadMagnet.getChannel(MagnetPowerSupply.CURRENT_STROBE_HANDLE).getId());
                    theDoc.scanStuff.scanVariableQuadY.setStrobeChannel(theDoc.selectedQuadMagnet.getChannel(MagnetPowerSupply.CURRENT_STROBE_HANDLE), 1);
                } else {
                    theDoc.scanStuff.scanVariableQuadY.removeStrobeChannel();
                }
            }
        });
        tmp_0.add(theDoc.scanStuff.scanControllerY.getJPanel());
        tmp_0.add(theDoc.scanStuff.avgCntrY.getJPanel(0));
        tmp_0.add(theDoc.scanStuff.vldCntrY.getJPanel());
        JScrollPane pvTextScrollPane = new JScrollPane(pvListTextAreaY);
        pvTextScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pvTextScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tmp_0.add(pvTextScrollPane);
        Border etchedBorder = BorderFactory.createEtchedBorder();
        scanYPanel.add(tmp_0, BorderLayout.WEST);
        scanYPanel.add(theDoc.scanStuff.graphScanY, BorderLayout.CENTER);
    }
