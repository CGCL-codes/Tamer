    private void handleChannelInit(ChannelInit e) {
        try {
            e.go();
            _thisChan = e.getChannel();
        } catch (AppiaEventException e1) {
            e1.printStackTrace();
        }
    }
