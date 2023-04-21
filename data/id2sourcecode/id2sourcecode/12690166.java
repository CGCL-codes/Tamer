    public Format setOutputFormat(Format format) {
        return new AudioFormat("LINEAR", inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits() > 0 ? inputFormat.getSampleSizeInBits() : 16, inputFormat.getChannels(), 0, 1);
    }
