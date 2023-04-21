    private void handleChannelInit(ChannelInit event) {
        try {
            _channel = event.getChannel();
            event.go();
        } catch (AppiaEventException ex) {
            ex.printStackTrace();
        }
    }
