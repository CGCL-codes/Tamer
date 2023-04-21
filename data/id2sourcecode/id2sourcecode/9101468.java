    @Override
    protected synchronized void openImpl() throws MidiUnavailableException {
        for (MergeInput input : inputs) {
            mergers.add(new Merger(input.getDevice(), input.getChannel()));
        }
    }
