    public void testFlatten2() {
        Send send = new Send();
        send.setSendChannel("subChannel1");
        Mixer mixer = getTestMixer(1, 2);
        mixer.getChannel(0).getPostEffects().addSend(send);
        mixer.getChannel(0).setOutChannel("subChannel2");
        mixer.getSubChannel(1).setLevel(-96.0f);
        MixerNode node = MixerNode.getMixerGraph(mixer);
        ArrayList list = new ArrayList();
        MixerNode.flattenToList(node, list);
        assertEquals("1", ((MixerNode) list.get(0)).channel.getName());
        assertEquals("subChannel1", ((MixerNode) list.get(1)).channel.getName());
        assertEquals("subChannel2", ((MixerNode) list.get(2)).channel.getName());
    }
