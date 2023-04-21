    @Override
    public void send(Packet packet) {
        OutgoingPacket p = (OutgoingPacket) packet;
        try {
            ioio_.protocol_.i2cWriteRead(twiNum_, p.tenBitAddr_, p.addr_, p.writeSize_, p.readSize_, p.writeData_);
        } catch (IOException e) {
            Log.e("TwiImpl", "Caught exception", e);
        }
    }
