    @Override
    public void generate(SoundBuffer soundBuffer) throws BadParameterException {
        super.generate(soundBuffer);
        for (int i = 0; i < soundBuffer.getNumberOfChannels(); i++) {
            for (int j = 0; j < soundBuffer.getDataLength(); j++) {
                soundBuffer.getChannelData(i)[j] = value;
            }
        }
    }
