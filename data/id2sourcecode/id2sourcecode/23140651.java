    private void connectLease() {
        if (this.pConn == null) {
            throw new PsEPRException("LeaseHandler.waitForLease: cannot wait because passed null connection handle");
        }
        if (this.channel == null) {
            throw new PsEPRException("LeaseHandler.waitForLease: cannot wait because passed null channel");
        }
        if (this.payloadParser == null) {
            throw new PsEPRException("LeaseHandler.waitForLease: cannot wait because passed null event parser");
        }
        if (this.eventReceiver == null) {
            throw new PsEPRException("LeaseHandler.waitForLease: cannot wait because passed null event receiver");
        }
        try {
            this.pLease = this.pConn.getLease(this.channel, this.payloadParser, this.eventReceiver);
        } catch (PsEPRException e) {
            this.pConn.close();
            throw e;
        }
        Thread checker = new Thread(new CheckConnection());
        checker.setName("LeaseHandler:" + this.pLease.getChannel());
        keepChecking = true;
        checker.start();
    }
