    private SynthDef[] createInputDefs(int[][] chMaps) {
        final int[] numInCh = new int[chMaps.length];
        final SynthDef[] defs;
        int numDefs = 0;
        numDefsLp: for (int i = 0; i < chMaps.length; i++) {
            numInCh[numDefs] = chMaps[i].length;
            for (int j = 0; j < numDefs; j++) {
                if (numInCh[j] == numInCh[numDefs]) continue numDefsLp;
            }
            numDefs++;
        }
        defs = new SynthDef[numDefs];
        for (int i = 0; i < numDefs; i++) {
            final Control ctrlI = Control.ir(new String[] { "i_aInBf", "i_aOtBs", "i_aPhBs", "i_intrp" }, new float[] { 0f, 0f, 0f, 1f });
            final GraphElem graph;
            if (numInCh[i] > 0) {
                final GraphElem phase = UGen.ar("In", ctrlI.getChannel("i_aPhBs"));
                final GraphElem bufRd = UGen.ar("BufRd", numInCh[i], ctrlI.getChannel("i_aInBf"), phase, UGen.ir(0f), ctrlI.getChannel("i_intrp"));
                final GraphElem out = UGen.ar("Out", ctrlI.getChannel("i_aOtBs"), bufRd);
                graph = out;
            } else {
                graph = ctrlI;
            }
            defs[i] = new SynthDef("eisk-input" + numInCh[i], graph);
        }
        return defs;
    }
