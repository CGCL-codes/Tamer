    private void mksummary() {
        summary = new JPanel();
        summary.setLayout(new GridLayout(8, 4, 2, 12));
        summary.setPreferredSize(new Dimension(400, 300));
        on = new JButton("On");
        off = new JButton("Off");
        freeze = new JButton("Freeze");
        reset = new JButton("Reset");
        reset.addActionListener(this);
        reset.setEnabled(true);
        cmd = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "AFF_Mode");
        buf = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "FdFwd2_Ctl");
        int cm = 0;
        try {
            cm = cmd.getRawValueRecord().intValue();
        } catch (ConnectionException ce) {
            myDoc.errormsg(ce + cmd.getId());
        } catch (GetException ge) {
            myDoc.errormsg(ge + cmd.getId());
        }
        mode(cm);
        on.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    cmd.putVal(2);
                } catch (ConnectionException ce) {
                    myDoc.errormsg("Error connection " + cmd.getId());
                } catch (PutException pe) {
                    myDoc.errormsg("Error write to PV " + cmd.getId());
                }
                mode(2);
            }
        });
        off.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    cmd.putVal(0);
                } catch (ConnectionException ce) {
                    myDoc.errormsg("Error connection " + cmd.getId());
                } catch (PutException pe) {
                    myDoc.errormsg("Error write to PV " + cmd.getId());
                }
                mode(0);
            }
        });
        freeze.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    cmd.putVal(1);
                } catch (ConnectionException ce) {
                    myDoc.errormsg("Error connection " + cmd.getId());
                } catch (PutException pe) {
                    myDoc.errormsg("Error write to PV " + cmd.getId());
                }
                mode(1);
            }
        });
        bfon = new JButton("On");
        bfoff = new JButton("Off");
        bfauto = new JButton("Auto");
        try {
            cm = buf.getRawValueRecord().intValue();
        } catch (ConnectionException ce) {
            myDoc.errormsg("Error connect " + buf.getId());
        } catch (GetException ge) {
            myDoc.errormsg("Error read " + buf.getId());
        }
        ctrl(cm);
        bfon.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    buf.putVal(1);
                } catch (ConnectionException ce) {
                    myDoc.errormsg("Error connection " + buf.getId());
                } catch (PutException pe) {
                    myDoc.errormsg("Error write to PV " + buf.getId());
                }
                ctrl(1);
            }
        });
        bfoff.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    buf.putVal(0);
                } catch (ConnectionException ce) {
                    myDoc.errormsg("Error connection " + buf.getId());
                } catch (PutException pe) {
                    myDoc.errormsg("Error write to PV " + buf.getId());
                }
                ctrl(0);
            }
        });
        bfauto.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    buf.putVal(2);
                } catch (ConnectionException ce) {
                    myDoc.errormsg("Error connection " + buf.getId());
                } catch (PutException pe) {
                    myDoc.errormsg("Error write to PV " + buf.getId());
                }
                ctrl(2);
            }
        });
        kip = new JButton("K,Kp,Ki");
        kip.setEnabled(true);
        kip.addActionListener(this);
        start = new JButton("Start");
        start.setEnabled(true);
        start.addActionListener(this);
        duration = new JButton("Duration");
        duration.setEnabled(true);
        duration.addActionListener(this);
        tshift = new JButton("TShift");
        tshift.setEnabled(true);
        tshift.addActionListener(this);
        max = new JButton("Max.");
        max.setEnabled(true);
        max.addActionListener(this);
        avg = new JButton("Wf. Avg");
        avg.setEnabled(true);
        avg.addActionListener(this);
        error = new JLabel("Peak err (%)");
        buffer = new JLabel("Second buffer");
        getallparameters();
        tfk = new DecimalField(affk, 5);
        tfkp = new DecimalField(affkp, 5);
        tfki = new DecimalField(affki, 5);
        tftimeshift = new DecimalField(afftimeshift, 5);
        tfstart = new DecimalField(affstart, 5);
        tfduration = new DecimalField(affduration, 5);
        tfmax = new DecimalField(affmax, 5);
        tfavg = new DecimalField(affavg, 5);
        tferr = new DecimalField(afferr, 5);
        JLabel dy1 = new JLabel("");
        JLabel dy2 = new JLabel("AFF Control");
        JLabel dy3 = new JLabel("");
        JLabel dy4 = new JLabel("");
        JLabel dy5 = new JLabel("");
        JLabel dy6 = new JLabel("");
        JLabel dy7 = new JLabel("");
        summary.add(dy1);
        summary.add(dy2);
        summary.add(dy3);
        summary.add(dy4);
        summary.add(on);
        summary.add(off);
        summary.add(freeze);
        summary.add(reset);
        summary.add(kip);
        summary.add(tfk);
        summary.add(tfkp);
        summary.add(tfki);
        summary.add(start);
        summary.add(tfstart);
        summary.add(duration);
        summary.add(tfduration);
        summary.add(tshift);
        summary.add(tftimeshift);
        summary.add(error);
        summary.add(tferr);
        summary.add(max);
        summary.add(tfmax);
        summary.add(avg);
        summary.add(tfavg);
        summary.add(buffer);
        summary.add(bfon);
        summary.add(bfoff);
        summary.add(bfauto);
        summary.add(dy5);
        summary.add(dy6);
        summary.add(dy7);
        if (peakMonitor != null) {
            peakMonitor.clear();
            peakMonitor = null;
        }
        Channel c1 = cf.getChannel(myDoc.cav[0].channelSuite().getChannel("cavAmpSet").getId().substring(0, 16) + "PeakErr");
        try {
            peakMonitor = c1.addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    afferr = newRecord.doubleValue();
                    if (afferr >= 2.0) tferr.setForeground(Color.RED); else if (afferr >= 1.0) tferr.setForeground(Color.BLUE); else tferr.setForeground(Color.BLACK);
                    tferr.setValue(afferr);
                }
            }, Monitor.VALUE);
        } catch (ConnectionException ce) {
            myDoc.errormsg("Error connect " + c1.getId());
        } catch (MonitorException me) {
            myDoc.errormsg("Error monitor " + c1.getId());
        }
        summary.setVisible(true);
    }
