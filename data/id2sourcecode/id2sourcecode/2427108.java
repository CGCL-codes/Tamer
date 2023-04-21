    public void decode(BitStream in, boolean commonWindow, DecoderConfig conf) throws AACException {
        if (conf.isScalefactorResilienceUsed() && rvlc == null) rvlc = new RVLC();
        final boolean er = conf.getProfile().isErrorResilientProfile();
        globalGain = in.readBits(8);
        if (!commonWindow) info.decode(in, conf, commonWindow);
        decodeSectionData(in, conf.isSectionDataResilienceUsed());
        decodeScaleFactors(in);
        pulseDataPresent = in.readBool();
        if (pulseDataPresent) {
            if (info.isEightShortFrame()) throw new AACException("pulse data not allowed for short frames");
            LOGGER.log(Level.FINE, "PULSE");
            decodePulseData(in);
        }
        tnsDataPresent = in.readBool();
        if (tnsDataPresent && !er) {
            if (tns == null) tns = new TNS();
            tns.decode(in, info);
        }
        gainControlPresent = in.readBool();
        if (gainControlPresent) {
            if (gainControl == null) gainControl = new GainControl(frameLength);
            LOGGER.log(Level.FINE, "GAIN");
            gainControl.decode(in, info.getWindowSequence());
        }
        if (conf.isSpectralDataResilienceUsed()) {
            int max = (conf.getChannelConfiguration() == ChannelConfiguration.CHANNEL_CONFIG_STEREO) ? 6144 : 12288;
            reorderedSpectralDataLen = Math.max(in.readBits(14), max);
            longestCodewordLen = Math.max(in.readBits(6), 49);
        } else decodeSpectralData(in);
    }
