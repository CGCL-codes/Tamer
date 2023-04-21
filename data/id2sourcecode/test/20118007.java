    private Mixer getTestMixer(int numChannels, int numSubChannels) {
        Mixer mixer = new Mixer();
        for (int i = 0; i < numChannels; i++) {
            Channel channel = new Channel();
            channel.setName(Integer.toString(i + 1));
            mixer.getChannels().addChannel(channel);
        }
        for (int i = 0; i < numSubChannels; i++) {
            Channel subChannel = new Channel();
            subChannel.setName("subChannel" + (i + 1));
            mixer.getSubChannels().addChannel(subChannel);
        }
        return mixer;
    }
