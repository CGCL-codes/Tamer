    protected void split(Crossover xo, AudioBuffer source, AudioBuffer low, AudioBuffer high) {
        for (int c = 0; c < source.getChannelCount(); c++) {
            xo.filter(source.getChannel(c), low.getChannel(c), high.getChannel(c), source.getSampleCount(), c);
        }
    }
