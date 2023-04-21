    public JPanel makeControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(4, 10, 2, 5));
        slcton = new JButton("On");
        slctoff = new JButton("Off");
        slctfreeze = new JButton("Freeze");
        slctreset = new JButton("Reset");
        slctupdate = new JButton("Update");
        allon = new JButton("On");
        alloff = new JButton("Off");
        allfreeze = new JButton("Freeze");
        allreset = new JButton("Reset");
        allupdate = new JButton("Update");
        allon.setForeground(Color.RED);
        allupdate.setForeground(Color.RED);
        alloff.setForeground(Color.RED);
        allfreeze.setForeground(Color.RED);
        allreset.setForeground(Color.RED);
        tfk = new DecimalField(affk, 5);
        tfkp = new DecimalField(affkp, 5);
        tfki = new DecimalField(affki, 5);
        tftimeshift = new DecimalField(afftimeshift, 5);
        tfstart = new DecimalField(affstart, 5);
        tfduration = new DecimalField(affduration, 5);
        tfmaxpulse = new DecimalField(affmax, 5);
        tfaverage = new DecimalField(affavg, 5);
        tflimit = new DecimalField(afflimit, 5);
        tfbuffer = new JTextField(5);
        lbk = new JLabel("     K");
        lbkp = new JLabel("     Kp");
        lbki = new JLabel("     Ki");
        lbstart = new JLabel("   Start");
        lbduration = new JLabel(" Duration");
        lbtimeshift = new JLabel("Time shift");
        lbmaxpulse = new JLabel("   Max.");
        lbaverage = new JLabel("Wf to Avg");
        lbbuffer = new JLabel("FF Buffer");
        lblimit = new JLabel("  Limit");
        slcton.setEnabled(true);
        slcton.addActionListener(this);
        slctoff.setEnabled(true);
        slctoff.addActionListener(this);
        slctfreeze.setEnabled(true);
        slctfreeze.addActionListener(this);
        slctreset.setEnabled(true);
        slctreset.addActionListener(this);
        slctupdate.setEnabled(true);
        slctupdate.addActionListener(this);
        allon.setEnabled(true);
        allon.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < myDoc.getSelector().allsclcavs; i++) {
                    ch = cf.getChannel(((RfCavity) myDoc.getSelector().allCavs.get(i)).channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Mode");
                    try {
                        ch.putVal(2);
                    } catch (ConnectionException ce) {
                        myDoc.errormsg("Error connect " + ch.getId());
                    } catch (PutException pe) {
                        myDoc.errormsg("Error write " + ch.getId());
                    }
                }
            }
        });
        alloff.setEnabled(true);
        alloff.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < myDoc.getSelector().allsclcavs; i++) {
                    ch = cf.getChannel(((RfCavity) myDoc.getSelector().allCavs.get(i)).channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Mode");
                    try {
                        ch.putVal(0);
                    } catch (ConnectionException ce) {
                        myDoc.errormsg("Error connection " + ch.getId());
                    } catch (PutException pe) {
                        myDoc.errormsg("Error write to PV " + ch.getId());
                    }
                }
            }
        });
        allfreeze.setEnabled(true);
        allfreeze.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < myDoc.getSelector().allsclcavs; i++) {
                    ch = cf.getChannel(((RfCavity) myDoc.getSelector().allCavs.get(i)).channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Mode");
                    try {
                        ch.putVal(1);
                    } catch (ConnectionException ce) {
                        myDoc.errormsg("Error connection " + ch.getId());
                    } catch (PutException pe) {
                        myDoc.errormsg("Error write to PV " + ch.getId());
                    }
                }
            }
        });
        allreset.setEnabled(true);
        allreset.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < myDoc.getSelector().allsclcavs; i++) {
                    ch = cf.getChannel(((RfCavity) myDoc.getSelector().allCavs.get(i)).channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Reset.PROC");
                    try {
                        ch.putVal(1);
                    } catch (ConnectionException ce) {
                        myDoc.errormsg("Error connection " + ch.getId());
                    } catch (PutException pe) {
                        myDoc.errormsg("Error write to PV " + ch.getId());
                    }
                }
            }
        });
        allupdate.setEnabled(true);
        allupdate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < myDoc.getSelector().allsclcavs; i++) {
                    if (tflimit.getValue() >= 0.2) myDoc.getSelector().cavTableModel.setValueAt(tflimit.getValue(), i, 13);
                    sclpv = ((RfCavity) myDoc.getSelector().allCavs.get(i)).channelSuite().getChannel("cavAmpSet").getId().substring(0, 16);
                    update();
                }
            }
        });
        JLabel dummy1 = new JLabel("Selected");
        JLabel dummy2 = new JLabel("");
        JLabel dummy3 = new JLabel("");
        JLabel dummy4 = new JLabel("");
        JLabel dummy5 = new JLabel("");
        JLabel dummy6 = new JLabel("");
        JLabel dummy7 = new JLabel("");
        JLabel dummy8 = new JLabel("");
        JLabel dummy9 = new JLabel("");
        JLabel dummya = new JLabel("All Cavity");
        dummya.setForeground(Color.RED);
        controlPanel.add(dummy1);
        controlPanel.add(slctoff);
        controlPanel.add(dummy5);
        controlPanel.add(slctfreeze);
        controlPanel.add(dummy2);
        controlPanel.add(slctreset);
        controlPanel.add(dummy3);
        controlPanel.add(slcton);
        controlPanel.add(dummy4);
        controlPanel.add(slctupdate);
        controlPanel.add(lbk);
        controlPanel.add(lbkp);
        controlPanel.add(lbki);
        controlPanel.add(lbstart);
        controlPanel.add(lbduration);
        controlPanel.add(lbtimeshift);
        controlPanel.add(lbmaxpulse);
        controlPanel.add(lbaverage);
        controlPanel.add(lbbuffer);
        controlPanel.add(lblimit);
        controlPanel.add(tfk);
        controlPanel.add(tfkp);
        controlPanel.add(tfki);
        controlPanel.add(tfstart);
        controlPanel.add(tfduration);
        controlPanel.add(tftimeshift);
        controlPanel.add(tfmaxpulse);
        controlPanel.add(tfaverage);
        controlPanel.add(tfbuffer);
        controlPanel.add(tflimit);
        controlPanel.add(dummya);
        controlPanel.add(alloff);
        controlPanel.add(dummy6);
        controlPanel.add(allfreeze);
        controlPanel.add(dummy7);
        controlPanel.add(allreset);
        controlPanel.add(dummy8);
        controlPanel.add(allon);
        controlPanel.add(dummy9);
        controlPanel.add(allupdate);
        controlPanel.setVisible(true);
        return controlPanel;
    }
