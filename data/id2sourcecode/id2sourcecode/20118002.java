    public void testGetMixerCode3() {
        Send send = new Send();
        send.setSendChannel("subChannel1");
        Mixer mixer = getTestMixer(1, 2);
        mixer.getChannel(0).getPostEffects().addSend(send);
        mixer.getChannel(0).setOutChannel("subChannel2");
        mixer.getSubChannel(1).setLevel(-96.0f);
        OpcodeList opcodeList = new OpcodeList();
        EffectManager manager = new EffectManager();
        int nchnls = 2;
        MixerNode node = MixerNode.getMixerGraph(mixer);
        String out = MixerNode.getMixerCode(mixer, opcodeList, manager, node, nchnls);
        String expected = "ga_bluesub_subChannel1_0\tsum\tga_bluesub_subChannel1_0, ga_bluemix_1_0\n" + "ga_bluesub_subChannel1_1\tsum\tga_bluesub_subChannel1_1, ga_bluemix_1_1\n" + "ga_bluesub_Master_0\tsum\tga_bluesub_Master_0, ga_bluesub_subChannel1_0\n" + "ga_bluesub_Master_1\tsum\tga_bluesub_Master_1, ga_bluesub_subChannel1_1\n";
        assertEquals(expected, out);
    }
