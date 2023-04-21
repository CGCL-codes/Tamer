    public client(Socket s, int playerID) {
        super(s, playerID);
        this.playerHostname = s.getInetAddress().getHostName();
        try {
            in = s.getInputStream();
            out = s.getOutputStream();
        } catch (Exception _ex) {
            log("error: fetching input/output streams (" + _ex.getMessage() + ")...");
            return;
        }
        outStream = new stream(new byte[bufferSize]);
        outStream.currentOffset = 0;
        inStream = new stream(new byte[bufferSize]);
        inStream.currentOffset = 0;
        readPtr = writePtr = 0;
        buffer = buffer = new byte[bufferSize];
    }
