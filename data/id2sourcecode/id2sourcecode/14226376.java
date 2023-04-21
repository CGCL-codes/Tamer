    private void testBLM(String lText) {
        initInSt = getInputStatValue();
        initChSt = getChanStatValue();
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
            PFstopLabel.setText(TestResult);
            PFstopLabel.validate();
            msgPane.validate();
            return;
        }
        IsStarted = 1;
        PFstopLabel.setText("");
        PFstopLabel.setVisible(true);
        PFstopLabel.validate();
        msgPane.validate();
        initPLL = getPllValue();
        try {
            pllWrapper.getChannel().putVal(1);
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
        try {
            cspWrapper.getChannel().putVal(1);
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
        int val = getHVRbValue();
        if (val > -900) {
            try {
                hvWrapper.getChannel().putVal(-1000);
            } catch (ConnectionException e) {
                System.err.println("Unable to connect to channel access.");
            } catch (PutException e) {
                System.err.println("Unable to set process variables.");
            }
        }
        try {
            pllWrapper.getChannel().putVal(.01);
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
        setTdValue(1);
        try {
            cspWrapper.getChannel().putVal(1);
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
        task.MPSwait();
        setTdValue(0);
        try {
            cspWrapper.getChannel().putVal(1);
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
        try {
            pllWrapper.getChannel().putVal(initPLL);
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
        try {
            cspWrapper.getChannel().putVal(1);
        } catch (ConnectionException e) {
            System.err.println("Unable to connect to channel access.");
        } catch (PutException e) {
            System.err.println("Unable to set process variables.");
        }
        int ChSt = getChanStatValue();
        if (ChSt == 0) TestResult = "Passed"; else TestResult = "Failed";
        PFstopLabel.setText(TestResult);
        PFstopLabel.validate();
        msgPane.validate();
    }
