    @Override
    public void complateRead(IOBuffer readBuf, IOBuffer writeBuf) throws Exception {
        if (currentState == READ_HEADER) {
            PacketHeader header = new PacketHeader();
            header.putBytes(readBuf.readBytes(0, readBuf.limit()));
            protocal.packetNum = (byte) (header.packetNum + 1);
            readBuf.position(0);
            readBuf.limit(header.packetLen);
            currentState = READ_BODY;
        } else {
            currentState = READ_HEADER;
            protocal.onPacketReceived(readBuf, writeBuf);
        }
    }
