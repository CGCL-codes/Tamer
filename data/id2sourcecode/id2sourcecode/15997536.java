    private TestMagPSPanel(Frame frame, Component locationComp, String title, Object[][] data, final String longValue, final String labelText, String selTwo) {
        super(frame, title, true);
        if (_channelWrappers == null) _channelWrappers = new ArrayList();
        if (MPSmagWrap == null) createChannelWrappers(labelText);
        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        listPane.setPreferredSize(new Dimension(300, 250));
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0, 30)));
        chanStatPanel = new JPanel();
        chanStatPanel.setLayout(new BoxLayout(chanStatPanel, BoxLayout.LINE_AXIS));
        chanStatPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        chanStatPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        MEBTmagLabels[0][0] = new JLabel("FPL_LDmp_chan_status ");
        chanStatPanel.add(MEBTmagLabels[0][0]);
        MEBTmagLabels[0][1] = new JLabel(String.valueOf(getChanStatValue()));
        chanStatPanel.add(MEBTmagLabels[0][1]);
        inputStatPanel = new JPanel();
        inputStatPanel.setLayout(new BoxLayout(inputStatPanel, BoxLayout.LINE_AXIS));
        inputStatPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        MEBTmagLabels[1][0] = new JLabel("FPL_LDmp_input_status ");
        inputStatPanel.add(MEBTmagLabels[1][0]);
        MEBTmagLabels[1][1] = new JLabel(getInputStatLabel());
        inputStatPanel.add(MEBTmagLabels[1][1]);
        inputStatPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        listPane.add(chanStatPanel);
        listPane.add(inputStatPanel);
        putMagLabels(MEBTmagLabels);
        String stat = getCCmdLabel();
        MagStatLabel = new JLabel("Magnet Status is " + stat);
        magStatPanel = new JPanel();
        magStatPanel.setLayout(new BoxLayout(magStatPanel, BoxLayout.LINE_AXIS));
        magStatPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        magStatPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        magStatPanel.add(MagStatLabel);
        listPane.add(magStatPanel);
        task = new LongTask(wrapper);
        JPanel readyPane = new JPanel();
        readyPane.setLayout(new FlowLayout());
        String srcChain = longValue + " " + selTwo;
        JLabel testLabel = new JLabel(srcChain);
        readyPane.add(testLabel);
        if (longValue.equals("MEBT_BS")) startButton = new JButton("Start"); else if (longValue.equals("CCL_BS") || longValue.equals("LDmp")) startButton = new JButton("On"); else startButton = new JButton("");
        if (getCCmdValue() == 0 && getChanStatValue() == 1) startButton.setVisible(false); else startButton.setVisible(true);
        startButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (getCCmdValue() == 0 && getChanStatValue() == 1) {
                    stopButton.setEnabled(false);
                    ;
                    return;
                }
                PFstopLabel.setText("");
                PFstopLabel.setVisible(false);
                PFstopLabel.validate();
                MsgPanel.validate();
                try {
                    ccmdWrapper.getChannel().putVal(2);
                } catch (ConnectionException e) {
                    System.err.println("Unable to connect to channel access.");
                } catch (PutException e) {
                    System.err.println("Unable to set process variables.");
                }
                try {
                    ccmdWrapper.getChannel().putVal(0);
                    if ((getCCmdLabel().equals("ON") && getChanStatValue() != 1) || getCCmdLabel().equals("?")) {
                        PFstopLabel.setHorizontalTextPosition(JLabel.CENTER);
                        PFstopLabel.setText(FailedStatus);
                    } else PFstopLabel.setText("Passed");
                    PFstopLabel.validate();
                    MsgPanel.validate();
                } catch (ConnectionException e) {
                    System.err.println("Unable to connect to channel access.");
                } catch (PutException e) {
                    System.err.println("Unable to set process variables.");
                }
                task.go(1);
                timer.start();
            }
        });
        if (longValue.equals("MEBT_BS")) stopButton = new JButton("Stop"); else if (longValue.equals("CCL_BS") || longValue.equals("LDmp")) stopButton = new JButton("Off"); else stopButton = new JButton("");
        stopPB = new JProgressBar(0, task.getLengthOfTask());
        stopPB.setValue(0);
        stopPB.setStringPainted(true);
        stopPB.setVisible(false);
        MsgPanel = new JPanel();
        MsgPanel.setLayout(new BoxLayout(MsgPanel, BoxLayout.LINE_AXIS));
        MsgPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        MsgPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        MsgPanel.add(PFstopLabel);
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));
        listPane.add(MsgPanel);
        stopButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (getCCmdValue() == 1 && getChanStatValue() == 0) return;
                PFstopLabel.setText("");
                PFstopLabel.setVisible(true);
                PFstopLabel.validate();
                MsgPanel.validate();
                try {
                    ccmdWrapper.getChannel().putVal(1);
                } catch (ConnectionException e) {
                    System.err.println("Unable to connect to channel access.");
                } catch (PutException e) {
                    System.err.println("Unable to set process variables.");
                }
                task.go(0);
                timer.start();
            }
        });
        if (longValue.equals("LDmp")) resetButton = new JButton("Reset"); else if (longValue.equals("CCL_BS")) resetButton = new JButton("Standby"); else resetButton = new JButton("");
        resetPB = new JProgressBar(0, task.getLengthOfTask());
        resetPB.setValue(0);
        resetPB.setStringPainted(true);
        resetPB.setVisible(false);
        resetButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PFresetLabel.setVisible(false);
                resetPB.setVisible(true);
                task.go(1);
                timer.start();
            }
        });
        logButton = new JButton("Log Report");
        logButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setVisible(false);
                String TestResult = PFstopLabel.getText();
                if (TestResult.length() > 0 && !TestResult.equals("Passed")) TestResult = "Failed";
                getMainWindow().publishMPSinputTestResultsToLogbook(labelText, TestResult, longValue);
            }
        });
        closeButton = new JButton("Close");
        closeButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        closeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setVisible(false);
            }
        });
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout());
        buttonPane.add(logButton);
        buttonPane.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonPane.add(closeButton);
        buttonPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        timer = new Timer(WAIT_TICS, new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                String Mstat = getCCmdLabel();
                progressbar = stopPB;
                if (Mstat.equals("ON") && getChanStatValue() == 1) {
                    progressbar = stopPB;
                    progressbar.setValue(task.getCurrent());
                } else if (Mstat.equals("OFF") && getChanStatValue() == 1) stopButton.setEnabled(false); else if (Mstat.equals("OFF") && getChanStatValue() == 0) stopButton.setEnabled(true);
                checkTask();
            }
        });
        JPanel magnetPane = new JPanel();
        magnetPane.setLayout(new BoxLayout(magnetPane, BoxLayout.LINE_AXIS));
        magnetPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        magnetPane.add(Box.createRigidArea(new Dimension(10, 0)));
        JPanel magnetPane2 = new JPanel();
        magnetPane2.setLayout(new BoxLayout(magnetPane2, BoxLayout.LINE_AXIS));
        magnetPane2.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        magnetPane2.add(Box.createRigidArea(new Dimension(10, 0)));
        label = new JLabel("Turn Magnet On");
        magnetPane2.add(label);
        magnetPane2.add(Box.createRigidArea(new Dimension(10, 0)));
        magnetPane2.add(startButton);
        magnetPane2.add(Box.createRigidArea(new Dimension(10, 0)));
        magnetPane3 = new JPanel();
        magnetPane3.setLayout(new BoxLayout(magnetPane3, BoxLayout.LINE_AXIS));
        magnetPane3.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        magnetPane3.add(Box.createRigidArea(new Dimension(10, 0)));
        label = new JLabel("Turn Magnet Off");
        magnetPane3.add(label);
        magnetPane3.add(Box.createRigidArea(new Dimension(10, 0)));
        magnetPane3.add(stopButton);
        magnetPane3.add(Box.createRigidArea(new Dimension(10, 0)));
        magnetPane3.add(stopPB);
        magnetPane3.setAlignmentX(Component.CENTER_ALIGNMENT);
        listPane.add(magnetPane);
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));
        listPane.add(magnetPane2);
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));
        listPane.add(magnetPane3);
        listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Container contentPane = getContentPane();
        contentPane.add(readyPane, BorderLayout.PAGE_START);
        contentPane.add(listPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);
        pack();
        setLocationRelativeTo(locationComp);
    }
