    public void testGetMixerGraph1() {
        Mixer mixer = new Mixer();
        Channel channel = new Channel();
        channel.setName("1");
        mixer.getChannels().addChannel(channel);
        MixerNode node = MixerNode.getMixerGraph(mixer);
        assertEquals(1, node.children.size());
        MixerNode mixerNode = ((MixerNode) node.children.get(0));
        assertEquals(channel, mixerNode.channel);
        assertEquals("1", mixerNode.channel.getName());
    }
