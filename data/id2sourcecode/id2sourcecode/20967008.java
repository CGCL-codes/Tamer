    private void nextDimmer() throws ShowFileException {
        int dimmerNumber = nextInteger("dimmer number", 1, getShow().getNumberOfDimmers());
        String name = nextString("dimmer name");
        int channelNumber = nextInteger("dimmer channel patch", 0, getShow().getNumberOfChannels());
        Dimmer dimmer = getShow().getDimmers().get(dimmerNumber - 1);
        dimmer.setName(name);
        if (channelNumber > 0) {
            Channel channel = getShow().getChannels().get(channelNumber - 1);
            dimmer.setChannel(channel);
        }
    }
