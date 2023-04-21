    private void unHookContainer() {
        for (ChannelEntry iteChannelEntry : broadcastList) {
            iteChannelEntry.getChannelProgram().unhook(iteChannelEntry.getContainer(), this);
        }
    }
