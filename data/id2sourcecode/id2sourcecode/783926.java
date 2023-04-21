    public MixingFloatAudioInputStream(AudioFormat audioFormat, Collection audioInputStreams) {
        super(new ByteArrayInputStream(new byte[0]), audioFormat, AudioSystem.NOT_SPECIFIED);
        audioInputStreamList = new ArrayList(audioInputStreams);
        mixBuffer = new FloatSampleBuffer(audioFormat.getChannels(), 0, audioFormat.getSampleRate());
        readBuffer = new FloatSampleBuffer();
        attenuationFactor = decibel2linear(-1.0f * attenuationPerStream * audioInputStreamList.size());
    }
