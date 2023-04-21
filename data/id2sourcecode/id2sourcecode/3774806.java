    public int getChannels() {
        try {
            return SoundFileUtilities.getNumberOfChannels(this.getFileName());
        } catch (IOException e) {
            return -1;
        } catch (UnsupportedAudioFileException e) {
            return -1;
        }
    }
