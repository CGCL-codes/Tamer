    private void nextGroup() throws ShowFileException {
        String name = nextString("group name");
        int channelCount = nextInteger("group channel count");
        Group group = new Group(dirty, name);
        getShow().getGroups().add(group);
        for (int i = 0; i < channelCount; i++) {
            int channelNumber = nextInteger("group channel number", 1, getShow().getNumberOfChannels());
            Channel channel = getShow().getChannels().get(channelNumber - 1);
            group.add(channel);
        }
    }
