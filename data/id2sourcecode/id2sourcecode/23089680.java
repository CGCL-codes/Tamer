    public Format setOutputFormat(Format format) {
        if (peer == null) throw new IllegalArgumentException("Must set Input Format first");
        AudioFormat inputFormat = (AudioFormat) format;
        return new AudioFormat("LINEAR", inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits() > 0 ? inputFormat.getSampleSizeInBits() : 16, inputFormat.getChannels(), 0, 1);
    }
