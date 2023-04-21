    static AudioInputStream prepareAudioStream(AudioInputStream ais, float maxRate) throws SmdiUnsupportedConversionException, AudioConversionException {
        AudioFormat af = ais.getFormat();
        if (af.getChannels() > 2) throw new SmdiUnsupportedConversionException("Too many channels in audio data");
        float rate = af.getSampleRate();
        if (rate > maxRate) rate = maxRate;
        return AudioConverter.convertStream(ais, new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, af.getChannels(), af.getChannels() * 2, af.getFrameRate(), true, af.properties()));
    }
