    protected void processTimingSignals(final Writer writer) throws IOException {
        final TimingCenterProcessor processor = new TimingCenterProcessor();
        final List<SignalEntry> signals = new ArrayList<SignalEntry>();
        final TimingCenter timingCenter = _sequence.getAccelerator().getTimingCenter();
        if (timingCenter == null) return;
        final Collection<String> handles = processor.getHandlesToProcess(timingCenter);
        for (Iterator<String> handleIter = handles.iterator(); handleIter.hasNext(); ) {
            final String handle = handleIter.next();
            final Channel channel = timingCenter.getChannel(handle);
            if (channel != null) {
                final String signal = channel.channelName();
                final SignalEntry entry = new SignalEntry(signal, handle);
                if (!signals.contains(entry)) {
                    signals.add(entry);
                }
            }
        }
        for (SignalEntry entry : signals) {
            final String line = processor.process(entry);
            writer.write(line);
            writer.write('\n');
        }
    }
