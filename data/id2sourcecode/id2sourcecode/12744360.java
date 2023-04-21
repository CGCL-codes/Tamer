    public void sequence() throws InvalidMfiDataException {
        int id = getAudioDataNumber();
        int format = getFormat();
        byte[] data = getData();
        AdpmMessage adpm = (AdpmMessage) subChunks.get(AdpmMessage.TYPE);
        int samplingRate = adpm.getSamplingRate() * 1000;
        int samplingBits = adpm.getSamplingBits();
        int channels = adpm.getChannels();
        AudioEngine engine = Factory.getAudioEngine(format);
        engine.setData(id, -1, samplingRate, samplingBits, channels, data, false);
    }
