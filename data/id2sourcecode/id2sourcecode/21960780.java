    protected JPanel makeSelectPanel() {
        done = new JButton("Accept selection");
        done.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                initCavity();
                myDoc.getController().excuteReset();
            }
        });
        toff = new JButton("Turn Off");
        toff.setForeground(Color.RED);
        toff.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < myDoc.numberOfCav; i++) {
                    try {
                        Channel LoopOff = cf.getChannel(myDoc.cav[i].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "LoopOff");
                        LoopOff.putVal("Close!");
                        Channel RFKill = cf.getChannel(myDoc.cav[i].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "RFKill");
                        RFKill.putVal("Kill");
                    } catch (ConnectionException ce) {
                        myDoc.errormsg("Error, cannot connect to PV!");
                    } catch (PutException pe) {
                        myDoc.errormsg("Error, cannot write to PV!");
                    }
                }
            }
        });
        ton = new JButton("Turn On");
        ton.setForeground(Color.RED);
        ton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < myDoc.numberOfCav; i++) {
                    try {
                        Channel ca2 = cf.getChannel(myDoc.cav[i].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "RunState");
                        ca2.putVal("Ramp");
                        Channel ca3 = cf.getChannel("SCL_HPRF:Tun" + myDoc.cav[i].channelSuite().getChannel("cavAmpSet").getId().substring(12, 16) + "Tun_Ctl");
                        ca3.putVal("Auto-Tune");
                    } catch (ConnectionException ce) {
                        myDoc.errormsg("Error, cannot connect to PV!");
                    } catch (PutException pe) {
                        myDoc.errormsg("Error, cannot write to PV!");
                    }
                }
            }
        });
        tune = new JButton("Tune Cavity");
        tune.setForeground(Color.RED);
        tune.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SCLCmTune scltun = new SCLCmTune(myDoc);
                if (myDoc.numberOfCav > 0) {
                    String[] phs = new String[myDoc.numberOfCav];
                    String bmgate = "ICS_Tim:Util:event46Count";
                    for (int i = 0; i < myDoc.numberOfCav; i++) {
                        phs[i] = myDoc.cav[i].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "Field_WfP";
                    }
                    if (scltun.setPV1(phs) && scltun.setPV3(bmgate)) {
                        Thread t = new Thread(scltun);
                        t.start();
                    }
                }
            }
        });
        setup = new JButton("Setup Cavity");
        setup.setForeground(Color.RED);
        setup.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < myDoc.numberOfCav; i++) {
                    try {
                        if (myDoc.cavPhaseSt[i] >= -180 && myDoc.cavPhaseSt[i] <= 180) myDoc.cav[i].setCavPhase(myDoc.cavPhaseSt[i]);
                        if (myDoc.designAmp[i] >= 4 && myDoc.designAmp[i] <= 25) myDoc.cav[i].setCavAmp(myDoc.designAmp[i]);
                    } catch (ConnectionException ce) {
                        myDoc.errormsg("Error, cannot connect to PV!");
                    } catch (PutException pe) {
                        myDoc.errormsg("Error, cannot write to PV!");
                    }
                }
            }
        });
        selectPanel = new JPanel();
        JScrollPane sp = new JScrollPane(cavTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JPanel controls = new JPanel();
        controls.setLayout(new GridLayout(12, 1));
        controls.setPreferredSize(new Dimension(180, 265));
        JLabel dmy2 = new JLabel("");
        JLabel dmy4 = new JLabel("");
        JLabel dmy5 = new JLabel("");
        JLabel dmy6 = new JLabel("");
        JLabel dmy7 = new JLabel("");
        JLabel dmy8 = new JLabel("");
        JLabel dmy9 = new JLabel("");
        controls.add(dmy2);
        controls.add(done);
        controls.add(dmy4);
        controls.add(dmy5);
        controls.add(toff);
        controls.add(dmy6);
        controls.add(tune);
        controls.add(dmy7);
        controls.add(dmy8);
        controls.add(setup);
        controls.add(dmy9);
        controls.add(ton);
        JPanel selPanel = new JPanel();
        BorderLayout bgl = new BorderLayout();
        selPanel.setLayout(bgl);
        selPanel.setPreferredSize(new Dimension(800, 300));
        selPanel.add(sp, BorderLayout.CENTER);
        selPanel.add(controls, BorderLayout.WEST);
        GridLayout gdl = new GridLayout(2, 1);
        selectPanel.setLayout(gdl);
        selectPanel.add(selPanel);
        selectPanel.setVisible(true);
        return selectPanel;
    }
