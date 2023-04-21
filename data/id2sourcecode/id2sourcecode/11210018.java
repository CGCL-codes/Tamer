    @Override
    public DataChannel<?> getChannel() {
        return UpdatesDataChannel.getUserChannel(user_name);
    }
