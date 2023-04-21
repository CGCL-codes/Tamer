    private void nextChannel() throws ShowFileException {
        int channelNumber = nextInteger("channel number", 1, getShow().getNumberOfChannels());
        String name = nextString("channel name");
        Channel channel = getShow().getChannels().get(channelNumber - 1);
        channel.setName(name);
    }
