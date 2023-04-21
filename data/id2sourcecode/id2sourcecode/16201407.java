    public void loadMIDI(String uid, URL url) throws Exception {
        Sequence sequence = MidiSystem.getSequence(url.openStream());
        midis.put(uid, sequence);
    }
