    private int getChannels(URI uri, ProgressMonitor mon) throws IOException, UnsupportedAudioFileException {
        File wavFile = DataSetURI.getFile(uri, mon);
        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(wavFile);
        AudioFormat audioFormat = fileFormat.getFormat();
        return audioFormat.getChannels();
    }
