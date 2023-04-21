    private void makeScanXPanel() {
        scanXPanel = new JPanel(new BorderLayout());
        scanXPanel.setPreferredSize(new Dimension(200, 400));
        JPanel tmp_0 = new JPanel();
        tmp_0.setLayout(new VerticalLayout());
        JPanel setStrobePanel = new JPanel();
        setStrobePanel.setLayout(new VerticalLayout());
        setStrobePanel.add(setStrobeRadioX);
        tmp_0.add(setStrobePanel);
        setStrobeRadioX.setEnabled(false);
        setStrobeRadioX.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (setStrobeRadioX.isSelected()) {
                    System.out.println("Strobe PV Name: " + theDoc.selectedQuadMagnet.getChannel(MagnetPowerSupply.CURRENT_STROBE_HANDLE).getId());
                    theDoc.scanStuff.scanVariableQuadX.setStrobeChannel(theDoc.selectedQuadMagnet.getChannel(MagnetPowerSupply.CURRENT_STROBE_HANDLE), 1);
                } else {
                    theDoc.scanStuff.scanVariableQuadX.removeStrobeChannel();
                }
            }
        });
        tmp_0.add(theDoc.scanStuff.scanControllerX.getJPanel());
        tmp_0.add(theDoc.scanStuff.avgCntrX.getJPanel(0));
        tmp_0.add(theDoc.scanStuff.vldCntrX.getJPanel());
        JScrollPane pvTextScrollPane = new JScrollPane(pvListTextAreaX);
        pvTextScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pvTextScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tmp_0.add(pvTextScrollPane);
        Border etchedBorder = BorderFactory.createEtchedBorder();
        scanXPanel.add(tmp_0, BorderLayout.WEST);
        scanXPanel.add(theDoc.scanStuff.graphScanX, BorderLayout.CENTER);
    }
