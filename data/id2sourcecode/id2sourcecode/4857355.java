    protected Frame createFrame(byte[] header, int headerLength) throws BEEPException {
        log.debug("--> createFrame");
        Frame f = Frame.parseHeader(this, header, headerLength);
        log.debug("<-- createFrame (Frame.isLast() == " + f.isLast() + ")");
        if (f.getSize() > ((ChannelImpl) f.getChannel()).getAvailableWindow()) {
            throw new BEEPException("Payload size is greater than channel " + "window size");
        }
        return f;
    }
