    public PatchNameMap(Synthesizer synthesizer, int channel) {
        Instrument[] loadedins = synthesizer.getLoadedInstruments();
        Instrument[] availins = synthesizer.getAvailableInstruments();
        Instrument[] availableInstruments;
        if (loadedins == availins) {
            availableInstruments = loadedins;
        } else {
            availableInstruments = new Instrument[loadedins.length + availins.length];
            int ix = 0;
            for (int i = 0; i < loadedins.length; i++) {
                availableInstruments[ix++] = loadedins[i];
            }
            for (int i = 0; i < availins.length; i++) {
                availableInstruments[ix++] = availins[i];
            }
        }
        Method getChannels = null;
        Method getKeys = null;
        if (availableInstruments.length > 0) {
            try {
                getChannels = availableInstruments[0].getClass().getMethod("getChannels");
            } catch (Exception e) {
            }
            try {
                getKeys = availableInstruments[0].getClass().getMethod("getKeys");
            } catch (Exception e) {
            }
        }
        for (Instrument instr : availableInstruments) {
            Patch p = instr.getPatch();
            boolean[] channels = null;
            if (getChannels != null) {
                try {
                    channels = (boolean[]) getChannels.invoke(instr);
                } catch (Exception e) {
                }
            }
            String[] keynames = null;
            if (getKeys != null) {
                try {
                    keynames = (String[]) getKeys.invoke(instr);
                } catch (Exception e) {
                }
            }
            if (channels == null || channels[channel]) {
                MyPatch patch = new MyPatch(p.getProgram(), p.getBank() >> 7, p.getBank() & 0x7f, instr);
                Node node = new Node(instr.getName(), patch);
                node.keynames = keynames;
                listAtLevel(0).add(node);
            } else {
            }
        }
    }
