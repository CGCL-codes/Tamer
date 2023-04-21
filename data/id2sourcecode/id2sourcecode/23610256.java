    protected void setChannel(int chan, SynthChannel synthChannel) {
        SynthChannel old = getChannel(chan);
        if (old != null && old instanceof AudioOutput) {
            removeAudioOutput((AudioOutput) old);
        }
        super.setChannel(chan, synthChannel);
        if (synthChannel != null && synthChannel instanceof AudioOutput) {
            addAudioOutput((AudioOutput) synthChannel);
        }
    }
