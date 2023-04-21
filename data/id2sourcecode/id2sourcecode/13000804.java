    @Override
    public Channel getChannel(final String handle) throws NoSuchChannelException {
        try {
            return super.getChannel(handle);
        } catch (NoSuchChannelException exception) {
            return getTrimSupply().getChannelSuite().getChannel(handle);
        }
    }
