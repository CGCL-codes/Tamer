    public void testGetMixerGraphChildStructure() {
        Mixer mixer = new Mixer();
        Channel channel = new Channel();
        channel.setName("1");
        mixer.getChannels().addChannel(channel);
        Channel subChannel1 = new Channel();
        subChannel1.setName("subChannel1");
        mixer.getSubChannels().addChannel(subChannel1);
        Channel subChannel2 = new Channel();
        subChannel2.setName("subChannel2");
        mixer.getSubChannels().addChannel(subChannel2);
        channel.setOutChannel(subChannel1.getName());
        subChannel1.setOutChannel(subChannel2.getName());
        MixerNode node = MixerNode.getMixerGraph(mixer);
        assertEquals(Channel.MASTER, node.channel.getName());
        assertEquals(1, node.children.size());
        node = (MixerNode) node.children.get(0);
        assertEquals(1, node.children.size());
        assertEquals(subChannel2, node.channel);
        node = (MixerNode) node.children.get(0);
        assertEquals(1, node.children.size());
        assertEquals(subChannel1, node.channel);
        node = (MixerNode) node.children.get(0);
        assertEquals(0, node.children.size());
        assertEquals(channel, node.channel);
    }
