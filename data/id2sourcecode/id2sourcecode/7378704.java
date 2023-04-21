    private ConfigMessage injectMp3Info(ConfigMessage config) {
        config.getProperties().put(this.DECODER_PROPERTY, getDecoderClassName());
        config.getProperties().put(Mp3Decoder.MP3_SAMPLE_RATE_PROPERTY, this.packetizer.getSampleFequency() + "");
        config.getProperties().put(Mp3Decoder.MP3_CHANNEL_COUNT_PROPERTY, this.packetizer.getChannelCount() + "");
        return config;
    }
