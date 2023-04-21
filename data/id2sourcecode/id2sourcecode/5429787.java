    private void handleChannelClose(ChannelClose ev) {
        try {
            ev.go();
        } catch (AppiaEventException e) {
            e.printStackTrace();
        }
        closeChannel.countDown();
        channels.remove(ev.getChannel());
        numberOfChannels = channels.size();
    }
