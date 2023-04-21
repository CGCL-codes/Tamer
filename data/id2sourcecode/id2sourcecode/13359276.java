    private void decode(SampleBuffer buffer) throws AACException {
        if (ADIFHeader.isPresent(in)) {
            adifHeader = ADIFHeader.readHeader(in);
            final PCE pce = adifHeader.getFirstPCE();
            config.setProfile(pce.getProfile());
            config.setSampleFrequency(pce.getSampleFrequency());
            config.setChannelConfiguration(ChannelConfiguration.forInt(pce.getChannelCount()));
        }
        if (!canDecode(config.getProfile())) throw new AACException("unsupported profile: " + config.getProfile().getDescription());
        syntacticElements.startNewFrame();
        try {
            syntacticElements.decode(in);
            syntacticElements.process(filterBank);
            syntacticElements.sendToOutput(buffer);
        } catch (AACException e) {
            buffer.setData(new byte[0], 0, 0, 0, 0);
            throw e;
        } catch (Exception e) {
            buffer.setData(new byte[0], 0, 0, 0, 0);
            throw new AACException(e);
        }
    }
