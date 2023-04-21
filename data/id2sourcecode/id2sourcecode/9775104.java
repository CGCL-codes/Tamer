    void send(GraphDiff diff, EventSubject subject, Object eventSource) {
        EventManager manager = (context.getChannel() != null) ? context.getChannel().getEventManager() : null;
        if (manager != null) {
            GraphEvent e = new GraphEvent(context, eventSource, diff);
            manager.postEvent(e, subject);
        }
    }
