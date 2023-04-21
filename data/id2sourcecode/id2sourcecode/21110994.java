    private AudioFormat[] getOutputFormats(AudioFormat inputFormat) {
        Vector formats = new Vector();
        AudioFormat format;
        int sampleSize = inputFormat.getSampleSizeInBits();
        boolean isBigEndian = inputFormat.isBigEndian();
        if (sampleSize == 8) {
            if (AudioFormat.Encoding.PCM_SIGNED.equals(inputFormat.getEncoding())) {
                format = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits(), inputFormat.getChannels(), inputFormat.getFrameSize(), inputFormat.getFrameRate(), false);
                formats.addElement(format);
            }
            if (AudioFormat.Encoding.PCM_UNSIGNED.equals(inputFormat.getEncoding())) {
                format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits(), inputFormat.getChannels(), inputFormat.getFrameSize(), inputFormat.getFrameRate(), false);
                formats.addElement(format);
            }
        } else if (sampleSize == 16) {
            if (AudioFormat.Encoding.PCM_SIGNED.equals(inputFormat.getEncoding()) && isBigEndian) {
                format = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits(), inputFormat.getChannels(), inputFormat.getFrameSize(), inputFormat.getFrameRate(), true);
                formats.addElement(format);
                format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits(), inputFormat.getChannels(), inputFormat.getFrameSize(), inputFormat.getFrameRate(), false);
                formats.addElement(format);
                format = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits(), inputFormat.getChannels(), inputFormat.getFrameSize(), inputFormat.getFrameRate(), false);
                formats.addElement(format);
            }
            if (AudioFormat.Encoding.PCM_UNSIGNED.equals(inputFormat.getEncoding()) && isBigEndian) {
                format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits(), inputFormat.getChannels(), inputFormat.getFrameSize(), inputFormat.getFrameRate(), true);
                formats.addElement(format);
                format = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits(), inputFormat.getChannels(), inputFormat.getFrameSize(), inputFormat.getFrameRate(), false);
                formats.addElement(format);
                format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits(), inputFormat.getChannels(), inputFormat.getFrameSize(), inputFormat.getFrameRate(), false);
                formats.addElement(format);
            }
            if (AudioFormat.Encoding.PCM_SIGNED.equals(inputFormat.getEncoding()) && !isBigEndian) {
                format = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits(), inputFormat.getChannels(), inputFormat.getFrameSize(), inputFormat.getFrameRate(), false);
                formats.addElement(format);
                format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits(), inputFormat.getChannels(), inputFormat.getFrameSize(), inputFormat.getFrameRate(), true);
                formats.addElement(format);
                format = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits(), inputFormat.getChannels(), inputFormat.getFrameSize(), inputFormat.getFrameRate(), true);
                formats.addElement(format);
            }
            if (AudioFormat.Encoding.PCM_UNSIGNED.equals(inputFormat.getEncoding()) && !isBigEndian) {
                format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits(), inputFormat.getChannels(), inputFormat.getFrameSize(), inputFormat.getFrameRate(), false);
                formats.addElement(format);
                format = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits(), inputFormat.getChannels(), inputFormat.getFrameSize(), inputFormat.getFrameRate(), true);
                formats.addElement(format);
                format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits(), inputFormat.getChannels(), inputFormat.getFrameSize(), inputFormat.getFrameRate(), true);
                formats.addElement(format);
            }
        }
        AudioFormat[] formatArray;
        synchronized (formats) {
            formatArray = new AudioFormat[formats.size()];
            for (int i = 0; i < formatArray.length; i++) {
                formatArray[i] = (AudioFormat) (formats.elementAt(i));
            }
        }
        return formatArray;
    }
