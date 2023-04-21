    public MidiSound(URL url) throws MidiUnavailableException, InvalidMidiDataException, IOException {
        load(url.openStream());
    }
