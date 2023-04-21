    private static LineAndStream getLineAndStream(AudioFormat format, Mixer mixer) {
        for (Line.Info lInfo : mixer.getTargetLineInfo()) {
            try {
                if (lInfo.getLineClass().equals(TargetDataLine.class)) {
                    DataLine.Info dInfo = (DataLine.Info) lInfo;
                    for (AudioFormat f : dInfo.getFormats()) {
                        if (AudioSystem.isConversionSupported(format, f)) {
                            try {
                                TargetDataLine candidate = (TargetDataLine) mixer.getLine(dInfo);
                                AudioFormat specifiedFormat = new AudioFormat(f.getEncoding(), format.getSampleRate(), f.getSampleSizeInBits(), f.getChannels(), f.getFrameSize(), format.getSampleRate(), f.isBigEndian());
                                candidate.open(specifiedFormat);
                                AudioInputStream baseStream = new AudioInputStream(candidate);
                                return new LineAndStream(candidate, AudioSystem.getAudioInputStream(format, baseStream));
                            } catch (IllegalArgumentException iae1) {
                                iae1.printStackTrace();
                            }
                        }
                    }
                }
            } catch (LineUnavailableException lue) {
                lue.printStackTrace();
            }
        }
        return null;
    }
