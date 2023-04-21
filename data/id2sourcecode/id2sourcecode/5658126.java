    private void update() {
        try {
            if (tfk.getValue() >= 0.) {
                ch = cf.getChannel(sclpv + "AFF_K");
                ch.putVal(tfk.getValue());
            }
            if (tfkp.getValue() >= 0.) {
                ch = cf.getChannel(sclpv + "AFF_Kp");
                ch.putVal(tfkp.getValue());
            }
            if (tfki.getValue() >= 0.) {
                ch = cf.getChannel(sclpv + "AFF_Ki");
                ch.putVal(tfki.getValue());
            }
            if (tftimeshift.getValue() >= 0.) {
                ch = cf.getChannel(sclpv + "AFF_Shift");
                ch.putVal(tftimeshift.getValue());
            }
            if (tfstart.getValue() >= 0.) {
                ch = cf.getChannel(sclpv + "AFF_Start");
                ch.putVal(tfstart.getValue());
            }
            if (tfduration.getValue() >= 0.) {
                ch = cf.getChannel(sclpv + "AFF_Offset");
                ch.putVal(tfduration.getValue());
                ch = cf.getChannel(sclpv + "CtlRFPW.PROC");
                ch.putVal(1);
            }
            if (tfmaxpulse.getValue() >= 0) {
                ch = cf.getChannel(sclpv + "AFFVetoMax");
                ch.putVal(tfmaxpulse.getValue());
            }
            if (tfaverage.getValue() >= 0) {
                ch = cf.getChannel(sclpv + "AFFAvgN");
                ch.putVal(tfaverage.getValue());
            }
            ch = cf.getChannel(sclpv + "FdFwd2_Ctl");
            String buffer2 = tfbuffer.getText().toLowerCase();
            if (buffer2.indexOf("on") != -1 || buffer2 == "1") ch.putVal(1); else if (buffer2.indexOf("off") != -1 || buffer2 == "0") ch.putVal(0); else ch.putVal(2);
        } catch (ConnectionException ce) {
            myDoc.errormsg("Error connection " + ch.getId());
        } catch (PutException pe) {
            myDoc.errormsg("Error write to PV " + ch.getId());
        }
    }
