    protected void init(boolean startThread, boolean writeSession) {
        log.debug(">>init(" + Boolean.valueOf(startThread) + ")");
        sesWorkQueue = new Vector(Integer.parseInt(props.getProperty(PROP_WORKQ_SIZE)));
        if (writeSession || !startThread) {
            sessions = new Hashtable(Integer.parseInt(props.getProperty(PROP_SESHASH_SIZE)));
            packetDB = new Hashtable(Integer.parseInt(props.getProperty(PROP_DBSIZE)));
            open();
            sesTimer = new TCPSessionTimer(this);
            sesTimer.start();
            log.debug("Start Thread = " + Boolean.valueOf(startThread));
            if (startThread) this.start();
        }
    }
