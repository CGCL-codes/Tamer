    Converter getConverter(AudioFormat desiredAudioFormat) throws LineUnavailableException {
        if (currentConverter != null && currentConverter.matches(desiredAudioFormat)) return currentConverter;
        AudioFormat minFormat = null;
        AudioFormatComparator comp = new AudioFormatComparator(desiredAudioFormat) {

            @Override
            public int conversionCompare(AudioFormat f1, AudioFormat f2) {
                boolean c1 = AudioSystem.isConversionSupported(f1, desiredAudioFormat);
                boolean c2 = AudioSystem.isConversionSupported(f2, desiredAudioFormat);
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
        if (preferredMixerInfo != null) {
            minfoList.remove(preferredMixerInfo);
            minfoList.add(0, preferredMixerInfo);
        }
        Mixer.Info[] minfo = minfoList.toArray(new Mixer.Info[minfoList.size()]);
        for (int i = 0; i < minfo.length; i++) {
            Mixer mixer = AudioSystem.getMixer(minfo[i]);
            Line.Info[] linfo = mixer.getSourceLineInfo();
            for (int j = 0; j < linfo.length; j++) {
                if (!(linfo[j] instanceof DataLine.Info)) {
                    continue;
                }
                DataLine.Info dinfo = (DataLine.Info) linfo[j];
                AudioFormat[] formats = dinfo.getFormats();
                for (int k = 0; k < formats.length; k++) {
                    AudioFormat f = formats[k];
                    if (comp.compare(f, minFormat) == -1) {
                        minFormat = f;
                    }
                }
            }
        }
        AudioFormat lineFormat = minFormat;
        if (lineFormat.getSampleRate() == AudioSystem.NOT_SPECIFIED) {
            lineFormat = new AudioFormat(lineFormat.getEncoding(), desiredAudioFormat.getSampleRate(), lineFormat.getSampleSizeInBits(), lineFormat.getChannels(), lineFormat.getFrameSize(), desiredAudioFormat.getFrameRate(), lineFormat.isBigEndian());
        }
        AudioFormat clf = AudioFormatComparator.channelFormat(lineFormat, desiredAudioFormat.getChannels());
        AudioFormat convertFormat = AudioSystem.isConversionSupported(clf, desiredAudioFormat) ? clf : desiredAudioFormat;
        if (lineFormat.getChannels() == 2 && desiredAudioFormat.getChannels() == 1) {
            switch(convertFormat.getFrameSize()) {
                case 1:
                    return new Converter1(desiredAudioFormat, convertFormat, lineFormat);
                case 2:
                    return new Converter2(desiredAudioFormat, convertFormat, lineFormat);
                default:
                    throw new LineUnavailableException("Cannot play " + desiredAudioFormat + " on audio device");
            }
        } else {
            return new Converter(desiredAudioFormat, convertFormat, lineFormat);
        }
    }
