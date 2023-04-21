    public void testGetMixerGraphSubChannel() {
        Mixer mixer = new Mixer();
        Channel channel = new Channel();
        channel.setName("1");
        mixer.getChannels().addChannel(channel);
        Channel subChannel1 = new Channel();
        subChannel1.setName("subChannel1");
        mixer.getSubChannels().addChannel(subChannel1);
        MixerNode node = MixerNode.getMixerGraph(mixer);
        assertEquals(2, node.children.size());
        MixerNode mixerNode = ((MixerNode) node.children.get(0));
        assertEquals(channel, mixerNode.channel);
        assertEquals("1", mixerNode.channel.getName());
        MixerNode mixerNode2 = ((MixerNode) node.children.get(1));
        assertEquals(subChannel1, mixerNode2.channel);
        assertEquals("subChannel1", mixerNode2.channel.getName());
    }
