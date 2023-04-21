    public void testGetMixerGraphSubChannelOrderFromSend() {
        Mixer mixer = new Mixer();
        Channel channel = new Channel();
        channel.setName("1");
        mixer.getChannels().addChannel(channel);
        Channel subChannel1 = new Channel();
        subChannel1.setName("subChannel1");
        mixer.getSubChannels().addChannel(subChannel1);
        Channel subChannel2 = new Channel();
        subChannel2.setName("subChannel2");
        Send send = new Send();
        send.setSendChannel("subChannel1");
        subChannel2.getPostEffects().addSend(send);
        mixer.getSubChannels().addChannel(subChannel2);
        MixerNode node = MixerNode.getMixerGraph(mixer);
        assertEquals(3, node.children.size());
        MixerNode mixerNode2 = ((MixerNode) node.children.get(1));
        assertEquals(subChannel2, mixerNode2.channel);
        assertEquals("subChannel2", mixerNode2.channel.getName());
        MixerNode mixerNode3 = ((MixerNode) node.children.get(2));
        assertEquals(subChannel1, mixerNode3.channel);
        assertEquals("subChannel1", mixerNode3.channel.getName());
    }
