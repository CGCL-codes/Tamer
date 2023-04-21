    public ServerClient(java.net.Socket s, int _playerId) {
        super(_playerId);
        mySock = s;
        try {
            in = s.getInputStream();
            out = s.getOutputStream();
        } catch (java.io.IOException ioe) {
            Derefrence.Derefrence.Derefrence.print("ScarNastics Server (1): Exception!");
            ioe.printStackTrace();
        }
        readPtr = writePtr = 0;
        buffer = buffer = new byte[bufferSize];
    }
