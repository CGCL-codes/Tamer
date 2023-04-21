    private void registerNodeChannels(final String type, final TypeQualifier nodeFilter) throws IOException {
        final NodeSignalProcessor processor = NodeSignalProcessor.getInstance(type);
        final List<SignalEntry> signals = new ArrayList<SignalEntry>();
        final TypeQualifier qualifier = QualifierFactory.qualifierForQualifiers(true, nodeFilter);
        final List<AcceleratorNode> nodes = SEQUENCE.getAllInclusiveNodesWithQualifier(qualifier);
        for (AcceleratorNode node : nodes) {
            final Collection handles = processor.getHandlesToProcess(node);
            for (Iterator handleIter = handles.iterator(); handleIter.hasNext(); ) {
                final String handle = (String) handleIter.next();
                final Channel channel = node.getChannel(handle);
                if (channel != null) {
                    final String signal = channel.channelName();
                    final SignalEntry entry = new SignalEntry(signal, handle);
                    if (!signals.contains(entry)) {
                        signals.add(entry);
                    }
                }
            }
        }
        for (SignalEntry entry : signals) {
            final MemoryProcessVariable pv = processor.makePV(SERVER, entry);
        }
    }
