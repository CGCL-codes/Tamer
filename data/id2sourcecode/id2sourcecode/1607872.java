    protected void registerTimingSignals() throws IOException {
        final TimingCenterProcessor processor = new TimingCenterProcessor();
        final List<SignalEntry> signals = new ArrayList<SignalEntry>();
        final TimingCenter timingCenter = SEQUENCE.getAccelerator().getTimingCenter();
        if (timingCenter == null) return;
        final Collection handles = processor.getHandlesToProcess(timingCenter);
        for (Iterator handleIter = handles.iterator(); handleIter.hasNext(); ) {
            final String handle = (String) handleIter.next();
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
            processor.makePV(SERVER, entry);
        }
    }
