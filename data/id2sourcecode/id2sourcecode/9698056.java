    @Override
    public SpiResult writeReadAsync(int slave, byte[] writeData, int writeSize, int totalSize, byte[] readData, int readSize) throws ConnectionLostException {
        checkState();
        SpiResult result = new SpiResult(readData);
        OutgoingPacket p = new OutgoingPacket();
        p.writeSize_ = writeSize;
        p.writeData_ = writeData;
        p.readSize_ = readSize;
        p.ssPin_ = indexToSsPin_[slave];
        p.totalSize_ = totalSize;
        if (p.readSize_ > 0) {
            synchronized (this) {
                pendingRequests_.add(result);
            }
        } else {
            result.ready_ = true;
        }
        try {
            outgoing_.write(p);
        } catch (IOException e) {
            Log.e("SpiMasterImpl", "Exception caught", e);
        }
        return result;
    }
