    private void dumpTraceElement(final TraceItem item) {
        assertNotNull("item", item);
        final TraceChannel channel = item.getChannel();
        if (channel != null && channel.isEnabled() && item.containsText()) {
            final Set traceWriters = channel.getTraceWriters();
            synchronized (traceWriters) {
                final Iterator iterator = traceWriters.iterator();
                if (iterator.hasNext() == false) {
                    defaultTraceWriter(item);
                } else {
                    while (iterator.hasNext()) {
                        ((TraceWriter) iterator.next()).write(item);
                    }
                }
            }
        }
        final Object lock = item.getLock();
        if (lock != null) {
            synchronized (lock) {
                lock.notify();
            }
            return;
        }
        disposeTraceItem(item);
    }
