        OutputSelectionAction(cDVSTest20.cDVSTestBiasgen.OutputMux m, int i) {
            super(m.getChannelName(i));
            mux = m;
            channel = i;
            m.addObserver(this);
        }
