    private void handleChannelInit(ChannelInit ev) {
        try {
            ev.go();
        } catch (AppiaEventException e) {
            e.printStackTrace();
        }
        channelList.add(ev.getChannel());
        if (timerChannel == null) {
            try {
                new HeartbeatTimer("heartbeattimer", 10000, ev.getChannel(), Direction.DOWN, this, EventQualifier.ON).go();
                timerChannel = ev.getChannel();
            } catch (AppiaEventException e) {
                e.printStackTrace();
            } catch (AppiaException e) {
                e.printStackTrace();
            }
        }
    }
