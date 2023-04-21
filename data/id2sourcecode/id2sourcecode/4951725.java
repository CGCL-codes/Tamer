    @Override
    public void open(IOBuffer readBuf, IOBuffer writeBuf) throws Exception {
        protocal = new MyBridgeProtocal(this);
        protocal.onSessionOpen(readBuf, writeBuf);
    }
