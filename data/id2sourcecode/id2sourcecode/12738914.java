    public void addChannelProgramEvent(ChannelProgramEvent ce) throws IndexOutOfBoundsException {
        ChannelProgram c = ce.getChannel();
        if (c.getTrack() < 0 || c.getTrack() > (channels.length - 1)) throw new IndexOutOfBoundsException();
        channels[c.getTrack()] = c.getChannel();
    }
