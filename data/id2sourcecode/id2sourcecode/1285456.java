    public final void reset() throws IOException {
        for (int channel = 0; channel < getChannelCount(); channel++) {
            write(channel, false);
        }
    }
