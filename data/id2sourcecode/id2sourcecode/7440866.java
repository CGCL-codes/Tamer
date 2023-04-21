    private void handleChannelClose(ChannelClose ev) {
        channelList.remove(ev.getChannel());
        if (timerChannel != null && timerChannel.equals(ev.getChannel())) {
            try {
                new HeartbeatTimer("heartbeattimer", 10000, ev.getChannel(), Direction.DOWN, this, EventQualifier.OFF).go();
                timerChannel = null;
            } catch (AppiaEventException e) {
                e.printStackTrace();
            } catch (AppiaException e) {
                e.printStackTrace();
            }
        }
        try {
            ev.go();
        } catch (AppiaEventException e) {
            e.printStackTrace();
        }
        if (channelList.size() > 0) {
            timerChannel = channelList.get(0);
            try {
                new HeartbeatTimer("heartbeattimer", 10000, timerChannel, Direction.DOWN, this, EventQualifier.ON).go();
            } catch (AppiaEventException e) {
                timerChannel = null;
                e.printStackTrace();
            } catch (AppiaException e) {
                timerChannel = null;
                e.printStackTrace();
            }
        }
    }
