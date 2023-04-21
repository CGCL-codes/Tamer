    private void ResetTimeoutTimer(long timeoutPeriod, long leaseInterval) {
        class InsertTimeout extends TimerTask {

            private String identifier;

            private PsEPRConnection connection;

            private String toChannel;

            private long leaseLong;

            public InsertTimeout(String id, PsEPRConnection pConn, String chan, long leaseL) {
                identifier = id;
                connection = pConn;
                toChannel = chan;
                leaseLong = leaseL;
            }

            public void run() {
                PsEPREvent myE = new PsEPREvent();
                PayloadLease myP = new PayloadLease(identifier, null, leaseOpTimeout, leaseLong);
                myE.setToChannel(toChannel);
                myE.setToService(connection.getServiceIdentity().getServiceName());
                myE.setPayload(myP);
                connection.insertEvent(myE);
            }
        }
        if (leaseTimer == null) {
            leaseTimer = new Timer();
        }
        long period = timeoutPeriod * 1000L;
        leaseTimeoutTime = System.currentTimeMillis() + period - 1000;
        log.log(log.LEASEDETAIL, "Scheduling timeout in " + Long.toString(period) + " milliseconds");
        leaseTimer.schedule(new InsertTimeout(leaseID, this.getConnection(), this.getChannel(), leaseInterval), period);
    }
