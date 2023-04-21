    private static AudioFormat getNativeAudioFormat(AudioFormat format) {
        Line.Info[] lineInfos = AudioSystem.getTargetLineInfo(new Line.Info(TargetDataLine.class));
        AudioFormat nativeFormat = null;
        for (int i = 0; i < lineInfos.length; i++) {
            AudioFormat[] formats = ((TargetDataLine.Info) lineInfos[i]).getFormats();
            for (int j = 0; j < formats.length; j++) {
                AudioFormat thisFormat = formats[j];
                if (thisFormat.getEncoding() == format.getEncoding() && thisFormat.getChannels() == format.getChannels() && thisFormat.isBigEndian() == format.isBigEndian() && thisFormat.getSampleSizeInBits() == format.getSampleSizeInBits() && thisFormat.getSampleRate() > format.getSampleRate()) {
                    nativeFormat = thisFormat;
                    break;
                }
            }
            if (nativeFormat != null) {
                break;
            }
        }
        return nativeFormat;
    }
