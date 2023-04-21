    private AudioFormat[] getOutputFormats(AudioFormat inputFormat) {
        Vector formats = new Vector();
        AudioFormat format;
        if ((inputFormat.getSampleSizeInBits() == 16) && AudioFormat.Encoding.PCM_SIGNED.equals(inputFormat.getEncoding())) {
            format = new AudioFormat(AudioFormat.Encoding.ULAW, inputFormat.getSampleRate(), 8, inputFormat.getChannels(), inputFormat.getChannels(), inputFormat.getSampleRate(), false);
            formats.addElement(format);
        }
        if (AudioFormat.Encoding.ULAW.equals(inputFormat.getEncoding())) {
            format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, inputFormat.getSampleRate(), 16, inputFormat.getChannels(), inputFormat.getChannels() * 2, inputFormat.getSampleRate(), false);
            formats.addElement(format);
            format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, inputFormat.getSampleRate(), 16, inputFormat.getChannels(), inputFormat.getChannels() * 2, inputFormat.getSampleRate(), true);
            formats.addElement(format);
        }
        AudioFormat[] formatArray = new AudioFormat[formats.size()];
        for (int i = 0; i < formatArray.length; i++) {
            formatArray[i] = (AudioFormat) (formats.elementAt(i));
        }
        return formatArray;
    }
