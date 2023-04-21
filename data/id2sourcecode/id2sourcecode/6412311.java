    public PVTrigger(String n) {
        super(n);
        trigChannel = Utilities.getChannelFactory().getChannel(n);
        trigChannel.connect();
        ivalm = new IEventSinkValue() {

            public void eventValue(ChannelRecord p1, Channel p2) {
                synchronized (isRunning) {
                    if (isRunning) {
                        Dbg.println("Firing event: " + eventID);
                        processJob();
                    }
                }
            }
        };
        try {
            trigChannel.addMonitorValue(ivalm, 1);
        } catch (Exception e) {
            Dbg.warnln(e.getMessage());
        }
    }
