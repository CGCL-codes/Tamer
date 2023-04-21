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
