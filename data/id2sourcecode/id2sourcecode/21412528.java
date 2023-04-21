    private void decodePCE(BitStream in) throws AACException {
        pce.decode(in);
        config.setProfile(pce.getProfile());
        config.setSampleFrequency(pce.getSampleFrequency());
        config.setChannelConfiguration(ChannelConfiguration.forInt(pce.getChannelCount()));
    }
