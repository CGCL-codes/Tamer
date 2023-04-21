    @Override
    public void send(Packet packet) {
        OutgoingPacket p = (OutgoingPacket) packet;
        try {
            ioio_.protocol_.spiMasterRequest(spiNum_, p.ssPin_, p.writeData_, p.writeSize_, p.totalSize_, p.readSize_);
        } catch (IOException e) {
            Log.e("SpiImpl", "Caught exception", e);
        }
    }
