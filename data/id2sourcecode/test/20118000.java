    public void testGetMixerCode() {
        Mixer mixer = getTestMixer(1, 1);
        mixer.getChannel(0).setLevel(-96.0f);
        mixer.getSubChannel(0).setLevel(-96.0f);
        OpcodeList opcodeList = new OpcodeList();
        EffectManager manager = new EffectManager();
        int nchnls = 2;
        MixerNode node = MixerNode.getMixerGraph(mixer);
        Send[] allSends = mixer.getAllSends();
        String output = MixerNode.getMixerCode(mixer, opcodeList, manager, node, nchnls);
        assertEquals("", output);
    }
