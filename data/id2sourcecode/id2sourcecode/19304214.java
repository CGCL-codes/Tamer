    public void add(AudioCodec targetAudioCodec, AudioFormat sourceFormat) {
        metaData.setAudioCodec(targetAudioCodec);
        metaData.setChannelType(ChannelType.lookup(sourceFormat.getChannels()));
        metaData.setRateType(RateType.lookup((int) sourceFormat.getSampleRate()));
        switch(targetAudioCodec) {
            case MP3:
                audioEncoder = new LameImp(sourceFormat);
                break;
            default:
                break;
        }
    }
