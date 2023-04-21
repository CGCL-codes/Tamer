    protected void processNodes(final Writer writer, final String type, final TypeQualifier nodeFilter) throws IOException {
        final NodeSignalProcessor processor = NodeSignalProcessor.getInstance(type);
        final List<SignalEntry> signals = new ArrayList<SignalEntry>();
        final TypeQualifier qualifier = QualifierFactory.qualifierForQualifiers(true, nodeFilter);
        final List<AcceleratorNode> nodes = _sequence.getAllInclusiveNodesWithQualifier(qualifier);
        for (AcceleratorNode node : nodes) {
            final Collection<String> handles = processor.getHandlesToProcess(node);
            for (Iterator<String> handleIter = handles.iterator(); handleIter.hasNext(); ) {
                final String handle = handleIter.next();
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
            final String line = processor.process(type, entry);
            writer.write(line);
            writer.write('\n');
        }
    }
