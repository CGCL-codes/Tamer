    public MidiFileFormat getMidiFileFormat(URL url) throws InvalidMidiDataException, IOException {
        InputStream is = url.openStream();
        try {
            return getMidiFileFormat(is);
        } finally {
            is.close();
        }
    }
