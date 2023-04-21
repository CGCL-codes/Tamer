    private void hookContainer() {
        for (ChannelEntry iteChannelEntry : broadcastList) {
            iteChannelEntry.getChannelProgram().hook(iteChannelEntry.getContainer(), this);
        }
    }
