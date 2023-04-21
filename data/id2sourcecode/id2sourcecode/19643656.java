    public MidiFileFormat getMidiFileFormat(URL url) throws InvalidMidiDataException, IOException {
        InputStream urlStream = url.openStream();
        BufferedInputStream bis = new BufferedInputStream(urlStream, bisBufferSize);
        MidiFileFormat fileFormat = null;
        try {
            fileFormat = getMidiFileFormat(bis);
        } finally {
            bis.close();
        }
        return fileFormat;
    }
