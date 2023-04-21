    private MidiEvent[] getPatchesAsMidiEvents() {
        ArrayList<MidiEvent> events = new ArrayList<MidiEvent>();
        for (int x = 0; x < patches.size(); x++) {
            PatchChange chg = (PatchChange) patches.get(x);
            events.add(chg.getAsMidiEvent());
            channels.remove(new Integer(chg.getChannel()));
        }
        for (int x = 0; x < channels.size(); x++) {
            PatchChange chg = new PatchChange(Settings.getCurrentPatch(), ((Integer) channels.get(x)).intValue());
            events.add(chg.getAsMidiEvent());
        }
        MidiEvent[] evts = new MidiEvent[events.size()];
        events.toArray(evts);
        return evts;
    }
