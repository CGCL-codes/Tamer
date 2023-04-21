    public void testGetMixerCode4() {
        Mixer mixer = getTestMixer(3, 2);
        mixer.getChannel(1).setOutChannel("subChannel1");
        mixer.getChannel(2).setOutChannel("subChannel1");
        OpcodeList opcodeList = new OpcodeList();
        EffectManager manager = new EffectManager();
        int nchnls = 2;
        MixerNode node = MixerNode.getMixerGraph(mixer);
        String out = MixerNode.getMixerCode(mixer, opcodeList, manager, node, nchnls);
        String expected = "ga_bluesub_subChannel1_0\tsum\tga_bluesub_subChannel1_0, ga_bluemix_2_0\n" + "ga_bluesub_subChannel1_1\tsum\tga_bluesub_subChannel1_1, ga_bluemix_2_1\n" + "ga_bluesub_subChannel1_0\tsum\tga_bluesub_subChannel1_0, ga_bluemix_3_0\n" + "ga_bluesub_subChannel1_1\tsum\tga_bluesub_subChannel1_1, ga_bluemix_3_1\n" + "ga_bluesub_Master_0\tsum\tga_bluesub_Master_0, ga_bluemix_1_0\n" + "ga_bluesub_Master_1\tsum\tga_bluesub_Master_1, ga_bluemix_1_1\n" + "ga_bluesub_Master_0\tsum\tga_bluesub_Master_0, ga_bluesub_subChannel1_0\n" + "ga_bluesub_Master_1\tsum\tga_bluesub_Master_1, ga_bluesub_subChannel1_1\n";
        assertEquals(expected, out);
    }
