    @Override
    public void advanceState() {
        for (int i = 0; i < nChannels; i++) {
            if (sources[i] != null) getChannel(i).add(sources[i].getValue()); else getChannel(i).add(0d);
        }
    }
