    private void handleChannelInit(ChannelInit event) {
        this.channel = event.getChannel();
        try {
            event.go();
        } catch (AppiaEventException ex) {
            ex.printStackTrace();
        }
    }
