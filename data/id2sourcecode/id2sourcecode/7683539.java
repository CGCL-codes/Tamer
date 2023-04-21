    private void createSong() {
        _song = new WAVESong();
        _song.setSampleRate(((WAVEFormatChunk) getChunk("fmt ")).getSampleRate());
        _song.setBitDepth(((WAVEFormatChunk) getChunk("fmt ")).getBitDepth());
        _song.setChannels(((WAVEDataChunk) (getChunk("data"))).getChannels());
    }
