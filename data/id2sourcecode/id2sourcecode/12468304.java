    ChannelIF[] getChannels() {
        Object input = viewer.getInput();
        if (input == null) {
            return new ChannelIF[0];
        } else {
            return (ChannelIF[]) input;
        }
    }
