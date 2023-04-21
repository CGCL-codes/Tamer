    public int getSampleRate() {
        if (header.computeNumChannels() == 0) {
            return -1;
        } else {
            return Integer.parseInt(header.getChannel(0).getNumSamples());
        }
    }
