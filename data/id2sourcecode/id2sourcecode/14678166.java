    @Override
    public Result writeReadAsync(int address, boolean tenBitAddr, byte[] writeData, int writeSize, byte[] readData, int readSize) throws ConnectionLostException {
        checkState();
        TwiResult result = new TwiResult(readData);
        OutgoingPacket p = new OutgoingPacket();
        p.writeSize_ = writeSize;
        p.writeData_ = writeData;
        p.tenBitAddr_ = tenBitAddr;
        p.readSize_ = readSize;
        p.addr_ = address;
        synchronized (this) {
            pendingRequests_.add(result);
            try {
                outgoing_.write(p);
            } catch (IOException e) {
                Log.e("SpiMasterImpl", "Exception caught", e);
            }
        }
        return result;
    }
