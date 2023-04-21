    public synchronized void dispatchAVEvent(Handler handler, IEvent event) {
        if (channel.equals(handler.getChannel())) {
            long t1 = sinks.get(0).getStream().getCreationTime();
            long t2 = handler.getCreationTime();
            long delta = t2 - t1;
            int t3 = ((IRTMPEvent) event).getTimestamp();
            t3 += delta;
            ((IRTMPEvent) event).setTimestamp(t3);
            sinks.get(0).getStream().dispatchStreamEvent(event);
        }
    }
