    static DecoderConfig parseMP4DecoderSpecificInfo(byte[] data) throws AACException {
        final BitStream in = new BitStream(data);
        final DecoderConfig config = new DecoderConfig();
        try {
            config.profile = readProfile(in);
            int sf = in.readBits(4);
            if (sf == 0xF) config.sampleFrequency = SampleFrequency.forFrequency(in.readBits(24)); else config.sampleFrequency = SampleFrequency.forInt(sf);
            config.channelConfiguration = ChannelConfiguration.forInt(in.readBits(4));
            switch(config.profile) {
                case AAC_SBR:
                    config.extProfile = config.profile;
                    config.sbrPresent = true;
                    sf = in.readBits(4);
                    config.downSampledSBR = config.sampleFrequency.getIndex() == sf;
                    config.sampleFrequency = SampleFrequency.forInt(sf);
                    config.profile = readProfile(in);
                    break;
                case AAC_MAIN:
                case AAC_LC:
                case AAC_SSR:
                case AAC_LTP:
                case ER_AAC_LC:
                case ER_AAC_LTP:
                case ER_AAC_LD:
                    config.frameLengthFlag = in.readBool();
                    if (config.frameLengthFlag) throw new AACException("config uses 960-sample frames, not yet supported");
                    config.dependsOnCoreCoder = in.readBool();
                    if (config.dependsOnCoreCoder) config.coreCoderDelay = in.readBits(14); else config.coreCoderDelay = 0;
                    config.extensionFlag = in.readBool();
                    if (config.extensionFlag) {
                        if (config.profile.isErrorResilientProfile()) {
                            config.sectionDataResilience = in.readBool();
                            config.scalefactorResilience = in.readBool();
                            config.spectralDataResilience = in.readBool();
                        }
                        in.skipBit();
                    }
                    if (config.channelConfiguration == ChannelConfiguration.CHANNEL_CONFIG_NONE) {
                        in.skipBits(3);
                        PCE pce = new PCE();
                        pce.decode(in);
                        config.profile = pce.getProfile();
                        config.sampleFrequency = pce.getSampleFrequency();
                        config.channelConfiguration = ChannelConfiguration.forInt(pce.getChannelCount());
                    }
                    if (in.getBitsLeft() > 10) readSyncExtension(in, config);
                    break;
                default:
                    throw new AACException("profile not supported: " + config.profile.getIndex());
            }
            return config;
        } finally {
            in.destroy();
        }
    }
