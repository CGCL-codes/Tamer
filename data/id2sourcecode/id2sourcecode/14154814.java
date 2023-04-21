    public double[] getInterleavedSamples(long begin, long end, double[] samples) throws IOException, IllegalArgumentException {
        long nbSamples = end - begin;
        long nbBytes = nbSamples * (getFormat().getSampleSizeInBits() / 8) * getFormat().getChannels();
        if (nbBytes > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Too many samples. Try using a smaller wav.");
        }
        byte[] inBuffer = new byte[(int) nbBytes];
        _audioInputStream.read(inBuffer, 0, inBuffer.length);
        decodeBytes(inBuffer, samples);
        return samples;
    }
