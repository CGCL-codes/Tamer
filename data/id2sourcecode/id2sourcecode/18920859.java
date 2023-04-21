    private DirectDLI createDataLineInfo(boolean isSource) {
        Vector formats = new Vector();
        AudioFormat[] hardwareFormatArray = null;
        AudioFormat[] formatArray = null;
        synchronized (formats) {
            nGetFormats(getMixerIndex(), getDeviceID(), isSource, formats);
            if (formats.size() > 0) {
                int size = formats.size();
                int formatArraySize = size;
                hardwareFormatArray = new AudioFormat[size];
                for (int i = 0; i < size; i++) {
                    AudioFormat format = (AudioFormat) formats.elementAt(i);
                    hardwareFormatArray[i] = format;
                    int bits = format.getSampleSizeInBits();
                    boolean isSigned = format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
                    boolean isUnsigned = format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED);
                    if ((isSigned || isUnsigned)) {
                        formatArraySize++;
                    }
                }
                formatArray = new AudioFormat[formatArraySize];
                int formatArrayIndex = 0;
                for (int i = 0; i < size; i++) {
                    AudioFormat format = hardwareFormatArray[i];
                    formatArray[formatArrayIndex++] = format;
                    int bits = format.getSampleSizeInBits();
                    boolean isSigned = format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
                    boolean isUnsigned = format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED);
                    if (bits == 8) {
                        if (isSigned) {
                            formatArray[formatArrayIndex++] = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, format.getSampleRate(), bits, format.getChannels(), format.getFrameSize(), format.getSampleRate(), format.isBigEndian());
                        } else if (isUnsigned) {
                            formatArray[formatArrayIndex++] = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), bits, format.getChannels(), format.getFrameSize(), format.getSampleRate(), format.isBigEndian());
                        }
                    } else if (bits > 8 && (isSigned || isUnsigned)) {
                        formatArray[formatArrayIndex++] = new AudioFormat(format.getEncoding(), format.getSampleRate(), bits, format.getChannels(), format.getFrameSize(), format.getSampleRate(), !format.isBigEndian());
                    }
                }
            }
        }
        if (formatArray != null) {
            return new DirectDLI(isSource ? SourceDataLine.class : TargetDataLine.class, formatArray, hardwareFormatArray, 32, AudioSystem.NOT_SPECIFIED);
        }
        return null;
    }
