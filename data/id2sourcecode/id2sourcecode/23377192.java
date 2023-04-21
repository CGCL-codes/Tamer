    protected void setLine(AudioFormat desiredAudioFormat) {
        if (currentAudioFormat != null && currentAudioFormat.matches(desiredAudioFormat)) return;
        AudioFormat minFormat = null;
        AudioFormatComparator comp = new AudioFormatComparator(desiredAudioFormat) {

            @Override
            public int conversionCompare(AudioFormat f1, AudioFormat f2) {
                boolean c1 = AudioSystem.isConversionSupported(desiredAudioFormat, f1);
                boolean c2 = AudioSystem.isConversionSupported(desiredAudioFormat, f2);
                if (c1) {
                    if (!c2) {
                        return -1;
                    }
                } else if (!c2) {
                    return 1;
                }
                return 0;
            }
        };
        ArrayList<Mixer.Info> minfoList = new ArrayList<Mixer.Info>(Arrays.asList(AudioSystem.getMixerInfo()));
        LOG.debug("[setLine](recorder) preferred mixer is: {0}", preferredMixerInfo);
        if (preferredMixerInfo != null) {
            minfoList.remove(preferredMixerInfo);
            minfoList.add(0, preferredMixerInfo);
        }
        Mixer.Info[] minfo = minfoList.toArray(new Mixer.Info[minfoList.size()]);
        for (int i = 0; i < minfo.length; i++) {
            Mixer mixer = AudioSystem.getMixer(minfo[i]);
            System.out.format("Mixer: %s%n", minfo[i].getName());
            Line.Info[] linfo = mixer.getTargetLineInfo();
            for (int j = 0; j < linfo.length; j++) {
                if (!(linfo[j] instanceof DataLine.Info)) {
                    continue;
                }
                DataLine.Info dinfo = (DataLine.Info) linfo[j];
                AudioFormat[] formats = dinfo.getFormats();
                for (int k = 0; k < formats.length; k++) {
                    AudioFormat f = formats[k];
                    if (comp.compare(f, minFormat) == -1) {
                        System.out.println("set minFormat to " + f + " on mixer " + mixer.getMixerInfo());
                        minFormat = f;
                    }
                }
            }
        }
        currentAudioFormat = desiredAudioFormat;
        if (lineFormat != null && !lineFormat.matches(minFormat)) {
            closeLine();
        }
        lineFormat = minFormat;
        if (lineFormat.getSampleRate() == AudioSystem.NOT_SPECIFIED) {
            lineFormat = new AudioFormat(lineFormat.getEncoding(), desiredAudioFormat.getSampleRate(), lineFormat.getSampleSizeInBits(), lineFormat.getChannels(), lineFormat.getFrameSize(), desiredAudioFormat.getFrameRate(), lineFormat.isBigEndian());
        }
        AudioFormat cdf = AudioFormatComparator.channelFormat(desiredAudioFormat, lineFormat.getChannels());
        convertFormat = AudioSystem.isConversionSupported(cdf, lineFormat) ? cdf : lineFormat;
    }
