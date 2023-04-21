    protected void startviewer() {
        String pv = pv1.substring(0, 16);
        cm = new Monitor[11];
        ca = new Channel[11];
        ca[0] = cf.getChannel(pv + "AFF_Mode");
        ca[1] = cf.getChannel(pv + "AFF_Start");
        ca[2] = cf.getChannel(pv + "AFF_Duration");
        ca[3] = cf.getChannel(pv + "AFF_K");
        ca[4] = cf.getChannel(pv + "AFF_Kp");
        ca[5] = cf.getChannel(pv + "AFF_Ki");
        ca[6] = cf.getChannel(pv + "AFF_Shift");
        ca[7] = cf.getChannel(pv + "AFFVetoMax");
        ca[8] = cf.getChannel(pv + "AFFAvgN");
        ca[9] = cf.getChannel(pv + "FdFwd2_Ctl");
        ca[10] = cf.getChannel(pv1);
        try {
            cm[0] = ca[0].addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    if (newRecord.intValue() == 2) myDoc.getSelector().cavTableModel.setValueAt("On", row, 2); else if (newRecord.intValue() == 0) myDoc.getSelector().cavTableModel.setValueAt("Off", row, 2); else myDoc.getSelector().cavTableModel.setValueAt("Freeze", row, 2);
                }
            }, Monitor.VALUE);
            cm[1] = ca[1].addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    myDoc.getSelector().cavTableModel.setValueAt(newRecord.doubleValue(), row, 3);
                }
            }, Monitor.VALUE);
            cm[2] = ca[2].addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    myDoc.getSelector().cavTableModel.setValueAt(newRecord.doubleValue(), row, 4);
                }
            }, Monitor.VALUE);
            cm[3] = ca[3].addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    myDoc.getSelector().cavTableModel.setValueAt(newRecord.doubleValue(), row, 5);
                }
            }, Monitor.VALUE);
            cm[4] = ca[4].addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    myDoc.getSelector().cavTableModel.setValueAt(newRecord.doubleValue(), row, 6);
                }
            }, Monitor.VALUE);
            cm[5] = ca[5].addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    myDoc.getSelector().cavTableModel.setValueAt(newRecord.doubleValue(), row, 7);
                }
            }, Monitor.VALUE);
            cm[6] = ca[6].addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    myDoc.getSelector().cavTableModel.setValueAt(newRecord.doubleValue(), row, 8);
                }
            }, Monitor.VALUE);
            cm[7] = ca[7].addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    myDoc.getSelector().cavTableModel.setValueAt(newRecord.intValue(), row, 9);
                }
            }, Monitor.VALUE);
            cm[8] = ca[8].addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    myDoc.getSelector().cavTableModel.setValueAt(newRecord.intValue(), row, 10);
                }
            }, Monitor.VALUE);
            cm[9] = ca[9].addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    if (newRecord.intValue() == 2) myDoc.getSelector().cavTableModel.setValueAt("Auto", row, 11); else if (newRecord.intValue() == 0) myDoc.getSelector().cavTableModel.setValueAt("Off", row, 11); else myDoc.getSelector().cavTableModel.setValueAt("On", row, 11);
                }
            }, Monitor.VALUE);
            cm[10] = ca[10].addMonitorValTime(new IEventSinkValTime() {

                public void eventValue(ChannelTimeRecord newRecord, Channel chan) {
                    myDoc.affpeak[row] = newRecord.doubleValue();
                    myDoc.getSelector().cavTableModel.setValueAt(myDoc.df.format(myDoc.affpeak[row]), row, 12);
                    if (myDoc.affpeak[row] >= 3. * myDoc.afflimit[row]) {
                        try {
                            ca[0].putVal(0);
                        } catch (ConnectionException ce) {
                        } catch (PutException pe) {
                        }
                        myDoc.errormsg("Alarm ! " + pv1);
                    } else if (myDoc.affpeak[row] >= 2. * myDoc.afflimit[row]) {
                        Channel c1 = cf.getChannel(pv1.substring(0, 16) + "AFF_Reset.PROC");
                        try {
                            c1.putVal(1);
                        } catch (ConnectionException ce) {
                        } catch (PutException pe) {
                        }
                    }
                }
            }, Monitor.VALUE);
        } catch (ConnectionException ce) {
            System.out.println(pv + " connect err");
        } catch (MonitorException me) {
            System.out.println(pv + " monitor err");
        }
    }
