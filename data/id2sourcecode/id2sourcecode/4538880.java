    private List getProgramNames() {
        List programNames = new ArrayList();
        if (!getChannel().isPercussionChannel()) {
            MidiInstrument[] instruments = TuxGuitar.instance().getPlayer().getInstruments();
            if (instruments != null) {
                int count = instruments.length;
                if (count > 128) {
                    count = 128;
                }
                for (int i = 0; i < count; i++) {
                    programNames.add(instruments[i].getName());
                }
            }
        }
        if (programNames.isEmpty()) {
            String programPrefix = TuxGuitar.getProperty("instrument.program");
            for (int i = 0; i < 128; i++) {
                programNames.add((programPrefix + " #" + i));
            }
        }
        return programNames;
    }
