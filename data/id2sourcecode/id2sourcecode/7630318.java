    public void improveQuality(boolean improve) {
        if (improve) {
            if (quality != 10) quality++;
        } else {
            if (quality != 0) quality--;
        }
        synchronized (speexEnc) {
            speexEnc.init(band, quality, (int) format.getSampleRate(), format.getChannels());
            speexEnc.getEncoder().setComplexity(2);
        }
    }
