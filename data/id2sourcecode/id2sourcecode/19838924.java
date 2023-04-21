    @Override
    public boolean Close() {
        addDebug(Level.INFO, "Quitting from channel %s", getChannel().getName());
        MessageQueue.addQueue(MessageQueueEnum.CHANNEL_USR_QUIT, getChannel());
        return super.Close();
    }
