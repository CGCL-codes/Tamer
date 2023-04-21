    protected long getChannelBufferMessageSize(MessageEvent arg1) throws Exception {
        Object o = arg1.getMessage();
        if (!(o instanceof ChannelBuffer)) {
            throw new InvalidClassException("Wrong object received in " + this.getClass().getName() + " codec " + o.getClass().getName());
        }
        ChannelBuffer dataBlock = (ChannelBuffer) o;
        return dataBlock.readableBytes();
    }
