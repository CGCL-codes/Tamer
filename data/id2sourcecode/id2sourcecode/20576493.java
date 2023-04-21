    private static void testBLM(String lText) {
        Channel ch;
        PFstopLabel.setText("");
        PFstopLabel.setVisible(true);
        PFstopLabel.validate();
        tstrslt.setText("");
        tstrslt.validate();
        msgPane.validate();
        initInSt = getInputStatValue(lText);
        initChSt = getChanStatValue(lText);
        if (initInSt == 0 || initChSt == 0) {
            TestResult = "Failed";
            PFstopLabel.setText(FailedStatus);
            PFstopLabel.validate();
            msgPane.validate();
            oldInSt = -1;
            IsStarted = 0;
            InSt1 = -1;
            InSt2 = -1;
            InSt3 = -1;
            String newMsg = FailedStatus + "\nInitially equals 0.";
            return;
        }
        IsStarted = 1;
        try {
            ch = cspWrapper.getChannel();
            ch.putVal(1);
            ch.flushIO();
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
        int val = getHVRbValue();
        if (val > -900) {
            try {
                ch = hvWrapper.getChannel();
                ch.putVal(-1000);
                ch.flushIO();
            } catch (ConnectionException e) {
                System.err.println("Unable to connect to channel access.");
            } catch (PutException e) {
                System.err.println("Unable to set process variables.");
            }
        }
        Iterator iter = MPSpllWrap.iterator();
        while (iter.hasNext()) {
            pllWrapper = (ChannelWrapper) iter.next();
            if (pllWrapper.getName().indexOf(lText) != -1) break;
        }
        try {
            ch = pllWrapper.getChannel();
            ch.putVal(.01);
            ch.flushIO();
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
        setTdValue(1);
        try {
            ch = cspWrapper.getChannel();
            ch.putVal(1);
            ch.flushIO();
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
        task.MPSwait();
        setTdValue(0);
        try {
            ch = cspWrapper.getChannel();
            ch.putVal(1);
            ch.flushIO();
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
        task.MPSwait();
        iter = MPSpllWrap.iterator();
        while (iter.hasNext()) {
            pllWrapper = (ChannelWrapper) iter.next();
            if (pllWrapper.getName().indexOf(lText) != -1) break;
        }
        String value = "" + pllWrapper.getFloatValue();
        initPLL = pllWrapper.getFloatValue();
        try {
            ch = pllWrapper.getChannel();
            ch.putVal(initPLL);
            ch.flushIO();
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
        try {
            ch = cspWrapper.getChannel();
            ch.putVal(1);
            ch.flushIO();
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
    }
