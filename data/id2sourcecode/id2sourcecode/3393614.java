    private int computeChunkSize(Format format) {
        if (format instanceof AudioFormat && (ulawFormat.matches(format) || linearFormat.matches(format))) {
            AudioFormat af = (AudioFormat) format;
            int units = af.getSampleSizeInBits() * af.getChannels() / 8;
            if (units == 0) units = 1;
            int chunks = (int) af.getSampleRate() * units / MAX_CHUNK_SIZE;
            return (chunks / units * units);
        }
        return Integer.MAX_VALUE;
    }
