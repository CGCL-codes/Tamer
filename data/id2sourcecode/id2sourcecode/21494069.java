    public void onMessageComplete(Channel channel) {
        channel.reactivate();
        Context.getInstance().getChannelManager().onReadRequired(channel);
    }
