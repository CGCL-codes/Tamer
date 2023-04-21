    public void testGetMixerCode2() {
        Send send = new Send();
        send.setSendChannel("subChannel1");
        Mixer mixer = getTestMixer(1, 1);
        mixer.getChannel(0).setLevel(-96.0f);
        mixer.getChannel(0).getPreEffects().addSend(send);
        mixer.getSubChannel(0).setLevel(-96.0f);
        OpcodeList opcodeList = new OpcodeList();
        EffectManager manager = new EffectManager();
        int nchnls = 2;
        MixerNode node = MixerNode.getMixerGraph(mixer);
        String out = MixerNode.getMixerCode(mixer, opcodeList, manager, node, nchnls);
        assertEquals("", out);
    }
