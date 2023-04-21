    @Override
    public void generate(SoundBuffer soundBuffer) throws BadParameterException {
        super.generate(soundBuffer);
        for (int channel = 0; channel < soundBuffer.getNumberOfChannels(); channel++) {
            for (int sample = 0; sample < soundBuffer.getDataLength(); sample++) {
                soundBuffer.getChannelData(channel)[sample] = source.getData()[channel][position[channel]];
                position[channel]++;
            }
        }
    }
