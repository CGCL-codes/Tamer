    private boolean createDefs(int numInputChannels) throws IOException {
        final Control ctrlI = Control.ir(new String[] { "i_aInBs", "i_aOtBf" }, new float[] { 0f, 0f });
        final GraphElem graph;
        final SynthDef def;
        if (numInputChannels > 0) {
            final GraphElem in = UGen.ar("In", numInputChannels, ctrlI.getChannel("i_aInBs"));
            final GraphElem out = UGen.ar("DiskOut", ctrlI.getChannel("i_aOtBf"), in);
            graph = out;
        } else {
            graph = ctrlI;
        }
        def = new SynthDef("eisk-rec" + numInputChannels, graph);
        def.send(server);
        return true;
    }
