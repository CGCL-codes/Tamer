    public void process(FilterBank filterBank) throws AACException {
        final Profile profile = config.getProfile();
        final SampleFrequency sf = config.getSampleFrequency();
        int chs = config.getChannelConfiguration().getChannelCount();
        if (chs == 1 && psPresent) chs++;
        final int mult = sbrPresent ? 2 : 1;
        if (data == null || chs != data.length || (mult * config.getFrameLength()) != data[0].length) data = new float[chs][mult * config.getFrameLength()];
        int channel = 0;
        Element e;
        SCE_LFE scelfe;
        CPE cpe;
        for (int i = 0; i < elements.length && channel < chs; i++) {
            e = elements[i];
            if (e == null) continue;
            if (e instanceof SCE_LFE) {
                scelfe = (SCE_LFE) e;
                channel += processSingle(scelfe, filterBank, channel, profile, sf);
            } else if (e instanceof CPE) {
                cpe = (CPE) e;
                processPair(cpe, filterBank, channel, profile, sf);
                channel += 2;
            } else if (e instanceof CCE) {
                ((CCE) e).process();
                channel++;
            }
        }
    }
