    public Decoder(byte[] decoderSpecificInfo) throws AACException {
        config = DecoderConfig.parseMP4DecoderSpecificInfo(decoderSpecificInfo);
        if (config == null) throw new IllegalArgumentException("illegal MP4 decoder specific info");
        if (!canDecode(config.getProfile())) throw new AACException("unsupported profile: " + config.getProfile().getDescription());
        syntacticElements = new SyntacticElements(config);
        filterBank = new FilterBank(config.isSmallFrameUsed(), config.getChannelConfiguration().getChannelCount());
        in = new BitStream();
        LOGGER.log(Level.FINE, "profile: {0}", config.getProfile());
        LOGGER.log(Level.FINE, "sf: {0}", config.getSampleFrequency().getFrequency());
        LOGGER.log(Level.FINE, "channels: {0}", config.getChannelConfiguration().getDescription());
    }
