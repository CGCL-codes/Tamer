    public void initPlayer(java.net.Socket s, int playID) {
        playerId = playID;
        mySock = s;
        try {
            in = s.getInputStream();
            out = s.getOutputStream();
        } catch (java.io.IOException ioe) {
            Print.general("Radiance Server: Exception!");
            destruct();
        }
        outStream = new Stream(new byte[bufferSize]);
        outStream.currentOffset = 0;
        inStream = new Stream(new byte[bufferSize]);
        inStream.currentOffset = 0;
        readPtr = writePtr = 0;
        buffer = new byte[bufferSize];
    }
