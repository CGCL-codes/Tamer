    public void onSessionOpen(IOBuffer readBuf, IOBuffer writeBuf) {
        PacketInit init = new PacketInit();
        writePacket(writeBuf, init);
    }
