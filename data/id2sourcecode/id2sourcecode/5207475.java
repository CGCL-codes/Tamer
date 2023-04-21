    public MidiFileFormat getMidiFileFormat(URL url) throws InvalidMidiDataException, IOException {
        InputStream inputStream = url.openStream();
        try {
            return getMidiFileFormat(inputStream);
        } finally {
            inputStream.close();
        }
    }
