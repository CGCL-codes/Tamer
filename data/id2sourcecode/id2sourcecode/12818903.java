    protected void init() {
        this.writer = connection.writer;
        done = false;
        writerThread = new Thread() {

            public void run() {
                writePackets(this);
            }
        };
        writerThread.setName("Smack Packet Writer (" + connection.connectionCounterValue + ")");
        writerThread.setDaemon(true);
    }
