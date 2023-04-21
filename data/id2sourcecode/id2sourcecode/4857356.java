    protected int getChannelAvailableWindow(int channel) throws BEEPException {
        ChannelImpl ch = (ChannelImpl) channels.get(Integer.toString(channel));
        if (ch == null) {
            throw new BEEPException("Session call on nonexistent channel.");
        }
        return ch.getAvailableWindow();
    }
