    private SynthDef[] createOutputDefs(int numOutputChannels) {
        final Control ctrlI = Control.ir(new String[] { "i_aInBs", "i_aOtBs" }, new float[] { 0f, 0f });
        final Control ctrlK = Control.kr(new String[] { "pos", "width", "orient", "volume" }, new float[] { 0f, 2f, 0f, 1f });
        final GraphElem graph;
        final SynthDef def;
        if (numOutputChannels > 0) {
            final GraphElem in = UGen.ar("In", ctrlI.getChannel("i_aInBs"));
            final GraphElem pan;
            if (numOutputChannels > 1) {
                pan = UGen.ar("PanAz", numOutputChannels, in, ctrlK.getChannel("pos"), ctrlK.getChannel("volume"), ctrlK.getChannel("width"), ctrlK.getChannel("orient"));
            } else {
                pan = UGen.ar("*", in, ctrlK.getChannel("volume"));
            }
            final GraphElem out = UGen.ar("Out", ctrlI.getChannel("i_aOtBs"), pan);
            graph = out;
        } else {
            graph = UGen.array(ctrlI, ctrlK);
        }
        def = new SynthDef("eisk-pan" + numOutputChannels, graph);
        return new SynthDef[] { def };
    }
